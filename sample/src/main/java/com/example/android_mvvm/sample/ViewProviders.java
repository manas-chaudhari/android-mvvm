package com.example.android_mvvm.sample;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.adapters.ViewProvider;

public class ViewProviders {

    @NonNull
    public static ViewProvider getItemListing() {
        return new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                return R.layout.row_item_with_image;
            }
        };
    }
}
