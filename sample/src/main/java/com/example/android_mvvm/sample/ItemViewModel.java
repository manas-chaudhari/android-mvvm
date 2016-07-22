package com.example.android_mvvm.sample;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.adapters.ShowMessage;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class ItemViewModel implements ViewModel {
    @NonNull
    public final String name;

    public final @DrawableRes int imageRes;

    public boolean hasImage() {
        return imageRes != 0;
    }

    @NonNull
    public final ReadOnlyField<Boolean> isSelected;

    public ItemViewModel(@NonNull final Item item,
                         Observable<ItemViewModel> selectedItem,
                         @NonNull final ShowMessage showMessage,
                         @NonNull final Navigator navigator,
                         @NonNull final Action1<ItemViewModel> onClicked) {
        this.isSelected = new ReadOnlyField<>(selectedItem.map(new Func1<ItemViewModel, Boolean>() {
            @Override
            public Boolean call(ItemViewModel itemViewModel) {
                return itemViewModel == ItemViewModel.this;
            }
        }));
        this.imageRes = item.name.contains("2") ? R.drawable.some_image : 0;
        this.name = item.name.toUpperCase();
        this.onClicked = new Action0() {
            @Override
            public void call() {
                onClicked.call(ItemViewModel.this);
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

    public static abstract class EventListener<T> {
        PublishSubject<Void> callbacks = PublishSubject.create();
        @NonNull public final T vm;
        final Observable<T> observable = callbacks.map(new Func1<Void, T>() {
            @Override
            public T call(Void aVoid) {
                return vm;
            }
        });

        protected EventListener(@NonNull T vm) {
            this.vm = vm;
        }

        abstract void call();
    }
}
