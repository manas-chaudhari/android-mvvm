package com.example.android_mvvm.sample;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.adapters.ShowMessage;

import rx.functions.Action0;

public class ItemViewModel implements ViewModel {
    @NonNull
    public final String name;

    public final @DrawableRes int imageRes;

    public boolean hasImage() {
        return imageRes != 0;
    }

    public ItemViewModel(@NonNull final Item item, @DrawableRes int imageRes, @NonNull final ShowMessage showMessage) {
        this.imageRes = imageRes;
        this.name = item.name.toUpperCase();
        this.onClicked = new Action0() {
            @Override
            public void call() {
                showMessage.show("Selected " + item.name);
            }
        };
    }

    @Nullable
    public final Action0 onClicked;
}
