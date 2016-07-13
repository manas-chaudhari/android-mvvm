package com.example.android_mvvm.testutils;

import rx.Observable;
import rx.functions.Action0;

public class SubscriptionCounter<T> implements Observable.Transformer<T, T> {
    public int subscriptions;
    public int unsubscriptions;

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                subscriptions++;
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                unsubscriptions++;
            }
        });
    }
}
