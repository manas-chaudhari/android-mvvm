package com.example.android_mvvm.adapters;

import android.databinding.ViewDataBinding;

import com.example.android_mvvm.ViewModel;

public class TestViewModelBinder implements ViewModelBinder {
    ViewDataBinding lastBinding;
    ViewModel lastViewModel;

    @Override
    public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
        lastBinding = viewDataBinding;
        lastViewModel = viewModel;
    }
}
