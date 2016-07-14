package com.example.android_mvvm.sample;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;

public class ItemViewModel implements ViewModel {
    @NonNull
    public final String name;

    public ItemViewModel(@NonNull Item item) {
        this.name = item.name.toUpperCase();
    }
}
