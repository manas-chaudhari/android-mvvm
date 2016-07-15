package com.example.android_mvvm.sample;

import android.support.annotation.NonNull;

public class Item {
    @NonNull
    public final String name;

    public Item(@NonNull String name) {
        this.name = name;
    }
}
