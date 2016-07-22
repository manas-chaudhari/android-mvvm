package com.example.android_mvvm.sample;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.adapters.ShowMessage;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

public class ItemViewModel implements ViewModel {
    @NonNull
    public final String name;

    public final @DrawableRes int imageRes;
    public ReadOnlyField<Boolean> isSelected;

    public boolean hasImage() {
        return imageRes != 0;
    }

    public ItemViewModel(@NonNull final Item item, @NonNull final ShowMessage showMessage, final Navigator navigator) {
        this.imageRes = item.name.contains("2") ? R.drawable.some_image : 0;
        this.name = item.name.toUpperCase();
        this.onDetailsClicked = new Action0() {
            @Override
            public void call() {
                navigator.openDetailsPage(item);
            }
        };
    }

    @NonNull
    public final PublishSubject<Void> onClicked = PublishSubject.create();

    @Nullable
    public final Action0 onDetailsClicked;

    public void setSelected(Observable<Boolean> selected) {
        this.isSelected = new ReadOnlyField<>(selected);
    }
}
