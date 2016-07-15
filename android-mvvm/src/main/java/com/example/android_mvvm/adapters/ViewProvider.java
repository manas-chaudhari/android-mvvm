package com.example.android_mvvm.adapters;

import android.support.annotation.IntegerRes;

import com.example.android_mvvm.ViewModel;

public interface ViewProvider {
    @IntegerRes int getView(ViewModel vm);
}
