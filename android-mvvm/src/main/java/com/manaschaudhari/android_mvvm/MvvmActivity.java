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
import android.support.v7.app.AppCompatActivity;

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;
import com.manaschaudhari.android_mvvm.utils.Preconditions;

/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library
 */
public abstract class MvvmActivity extends AppCompatActivity {
    private ViewDataBinding binding;
    private ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            binding = DataBindingUtil.setContentView(this, getLayoutId());
            getDefaultBinder().bind(binding, vm = createViewModel());

            if (vm != null)
                vm.onCreate();
        }
    }

    @Override
    protected void onStop() {
        if (vm != null)
            vm.onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
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
    protected void onResume() {
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
    protected void onDestroy() {
        if (vm != null)
            vm.onDestroy();

        if (binding != null) {
            getDefaultBinder().bind(binding, null);
            binding.executePendingBindings();
        }

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
