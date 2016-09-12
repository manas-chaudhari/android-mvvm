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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        getDefaultBinder().bind(binding, createViewModel());
    }

    @Override
    protected void onDestroy() {
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

    @NonNull
    protected abstract ViewModel createViewModel();

    @LayoutRes
    protected abstract int getLayoutId();
}
