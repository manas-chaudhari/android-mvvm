package com.manaschaudhari.android_mvvm.adapters;

import android.support.annotation.LayoutRes;

import com.manaschaudhari.android_mvvm.ViewModel;

public interface ViewProvider {
    @LayoutRes int getView(ViewModel vm);
}
