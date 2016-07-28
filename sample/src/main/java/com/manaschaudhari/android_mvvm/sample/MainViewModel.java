package com.manaschaudhari.android_mvvm.sample;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;

import rx.functions.Action0;

public class MainViewModel implements ViewModel {
    @NonNull
    private final Navigator navigator;

    public MainViewModel(@NonNull Navigator navigator) {

        this.navigator = navigator;
    }

    public final Action0 onAdapterClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToAdapterDemo();
        }
    };

    public final Action0 onFunctionalClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToFunctionalDemo();
        }
    };

    public final Action0 onTwoWayBindingClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToTwoWayBindingDemo();
        }
    };
}
