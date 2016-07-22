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

    public ItemViewModel(@NonNull final Item item, @NonNull final ShowMessage showMessage, final Navigator navigator) {
        this.imageRes = item.name.contains("2") ? R.drawable.some_image : 0;
        this.name = item.name.toUpperCase();
        this.onClicked = new Action0() {
            @Override
            public void call() {
                showMessage.show("Selected " + item.name);
            }
        };
        this.onDetailsClicked = new Action0() {
            @Override
            public void call() {
                navigator.openDetailsPage(item);
            }
        };
    }

    @Nullable
    public final Action0 onClicked;

    @Nullable
    public final Action0 onDetailsClicked;
}
