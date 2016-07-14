package com.example.android_mvvm.sample;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;

public class ItemViewModel implements ViewModel {
    @NonNull
    public final String name;

    public final @DrawableRes int imageRes;

    public boolean hasImage() {
        return imageRes != 0;
    }

    public ItemViewModel(@NonNull Item item, @DrawableRes int imageRes) {
        this.imageRes = imageRes;
        this.name = item.name.toUpperCase();
    }
}
