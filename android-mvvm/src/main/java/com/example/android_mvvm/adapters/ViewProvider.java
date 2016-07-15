package com.example.android_mvvm.adapters;

import android.support.annotation.LayoutRes;

import com.example.android_mvvm.ViewModel;

public interface ViewProvider {
    @LayoutRes int getView(ViewModel vm);
}
