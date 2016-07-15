package com.example.android_mvvm.adapters;

import android.databinding.ViewDataBinding;

import com.example.android_mvvm.ViewModel;

public interface ViewModelBinder {
    void bind(ViewDataBinding viewDataBinding, ViewModel viewModel);
}
