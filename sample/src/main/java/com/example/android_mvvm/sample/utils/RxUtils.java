package com.example.android_mvvm.sample.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class RxUtils {
    @NonNull
    public static <T> Pair<Observable<T>, Observable<Boolean>> trackActivity(@NonNull final Observable<T> source) {
        final BehaviorSubject<Integer> count = BehaviorSubject.create(0);
        return new Pair<>(
                Observable.using(new Func0<Integer>() {
                    @Override
                    public Integer call() {
                        count.onNext(count.getValue() + 1);
                        return null;
                    }
                }, new Func1<Integer, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Integer integer) {
                        return source;
                    }
                }, new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        count.onNext(count.getValue() - 1);
                    }
                }),
                count.map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 0;
                    }
                }));
    }
}
