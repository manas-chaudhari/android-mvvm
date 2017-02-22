/*
 * Copyright 2016 Manas Chaudhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manaschaudhari.android_mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manaschaudhari.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ViewPagerAdapter extends PagerAdapter implements Connectable {

    @NonNull
    private List<ViewModel> latestViewModels = new ArrayList<>();

    @NonNull
    private final Observable<List<ViewModel>> source;

    @NonNull
    private final ViewProvider viewProvider;

    @NonNull
    private final ViewModelBinder binder;

    public ViewPagerAdapter(@NonNull Observable<List<ViewModel>> viewModels, @NonNull ViewProvider viewProvider, @NonNull ViewModelBinder binder) {
        source = viewModels
                .doOnNext(new Consumer<List<ViewModel>>() {
                    @Override
                    public void accept(List<ViewModel> viewModels) throws Exception {
                        latestViewModels = (viewModels != null) ? viewModels : new ArrayList<ViewModel>();
                        notifyDataSetChanged();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ViewPagerAdapter", "Error in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<List<ViewModel>>empty())
                .share();
        this.viewProvider = viewProvider;
        this.binder = binder;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewModel vm = latestViewModels.get(position);
        int layoutId = viewProvider.getView(vm);
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(container.getContext()),
                layoutId,
                container,
                false);
        binder.bind(binding, vm);
        container.addView(binding.getRoot());
        return binding;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewDataBinding binding = (ViewDataBinding) object;
        binder.bind(binding, null);
        container.removeView(binding.getRoot());
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return latestViewModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((ViewDataBinding)object).getRoot() == view;
    }

    @Override
    public Disposable connect() {
        return source.subscribe();
    }
}
