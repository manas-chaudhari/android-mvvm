package com.manaschaudhari.android_mvvm.adapters;

import android.databinding.ViewDataBinding;

import com.manaschaudhari.android_mvvm.ViewModel;

public interface ViewModelBinder {
    void bind(ViewDataBinding viewDataBinding, ViewModel viewModel);
}
