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

package com.manaschaudhari.android_mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;
import com.manaschaudhari.android_mvvm.utils.Preconditions;

/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library
 */
public abstract class MvvmFragment extends Fragment {
    private ViewDataBinding binding;
    private ViewModel vm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        getDefaultBinder().bind(binding, vm = createViewModel());
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (vm != null)
            vm.onCreate();
    }

    @Override
    public void onStop() {
        if (vm != null)
            vm.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        if (vm != null)
            vm.onStart();
        super.onStart();
    }

    @Override
    public void onPause() {
        if (vm != null)
            vm.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (vm != null)
            vm.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        if (vm != null)
            vm.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        if (vm != null)
            vm.onDestroy();

        getDefaultBinder().bind(binding, null);
        binding.executePendingBindings();
        super.onDestroy();
    }

    @NonNull
    private ViewModelBinder getDefaultBinder() {
        ViewModelBinder defaultBinder = BindingUtils.getDefaultBinder();
        Preconditions.checkNotNull(defaultBinder, "Default Binder");
        return defaultBinder;
    }

    /**
     * Subclasses should create their ViewModel here.
     *
     * @return An instantiated ViewModel.
     */
    @NonNull
    protected abstract ViewModel createViewModel();

    /**
     * Subclasses should return the ID of the layout which they
     * would like to inflate.
     *
     * @return The ID of the layout to be inflated.
     */
    @LayoutRes
    protected abstract int getLayoutId();
}
