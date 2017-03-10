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

package com.manaschaudhari.android_mvvm.sample;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.adapters.ViewProvider;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;

import java.util.List;

import io.reactivex.functions.Action;
import rx.functions.Action0;

@SuppressWarnings("unused")
public class BindingAdapters {

    @NonNull
    public static final ViewModelBinder defaultBinder = new ViewModelBinder() {
        @Override
        public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
            viewDataBinding.setVariable(BR.vm, viewModel);
        }
    };

    @BindingAdapter("android:visibility")
    public static void bindVisibility(@NonNull View view, @Nullable Boolean visible) {
        int visibility = (visible != null && visible) ? View.VISIBLE : View.GONE;
        view.setVisibility(visibility);
    }


    /**
     * Binding Adapter Wrapper for checking memory leak
     */
    @BindingAdapter({"items", "view_provider"})
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, io.reactivex.Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        RecyclerView.Adapter previousAdapter = recyclerView.getAdapter();
        BindingUtils.bindAdapterWithDefaultBinder(recyclerView, items, viewProvider);

        // Previous adapter should get deallocated
        if (previousAdapter != null)
            ExampleApplication.getRefWatcher(recyclerView.getContext()).watch(previousAdapter);
    }

    /**
     * Binding Adapter Wrapper for checking memory leak
     */
    @BindingAdapter({"items", "view_provider"})
    public static void bindViewPagerAdapter(ViewPager viewPager, io.reactivex.Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        PagerAdapter previousAdapter = viewPager.getAdapter();
        BindingUtils.bindAdapterWithDefaultBinder(viewPager, items, viewProvider);

        // Previous adapter should get deallocated
        if (previousAdapter != null)
            ExampleApplication.getRefWatcher(viewPager.getContext()).watch(previousAdapter);
    }

    @BindingConversion
    public static View.OnClickListener toOnClickListener(final Action listener) {
        if (listener != null) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        listener.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            return null;
        }
    }

    @BindingConversion
    public static View.OnClickListener toOnClickListener(final Action0 listener) {
        if (listener != null) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.call();
                }
            };
        } else {
            return null;
        }
    }
}
