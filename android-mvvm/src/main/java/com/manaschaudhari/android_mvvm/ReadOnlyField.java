package com.manaschaudhari.android_mvvm;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class ReadOnlyField<T> extends ObservableField<T> {
    final Observable<T> source;
    final HashMap<OnPropertyChangedCallback, Subscription> subscriptions = new HashMap<>();

    public ReadOnlyField(@NonNull Observable<T> source) {
        this.source = source
                .doOnNext(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        set(t);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("ReadOnlyField", "onError in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<T>empty())
                .share();
    }

    /**
     * Calling set method of ReadOnlyField can result in weird behaviour
     * TODO: Document why set() shouldn't be used
     */
    @Override
    public void set(T value) {
        super.set(value);
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        subscriptions.put(callback, source.subscribe());
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        Subscription subscription = subscriptions.remove(callback);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
