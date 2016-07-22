package com.example.android_mvvm;

import java.util.HashMap;

import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;

public class LazyReadOnlyField<T> extends ReadOnlyField<T> {
    final HashMap<OnPropertyChangedCallback, Subscription> subscriptions = new HashMap<>();

    ReplaySubject<Observable<T>> replay;

    private LazyReadOnlyField(ReplaySubject<Observable<T>> replay) {
        super(Observable.switchOnNext(replay));
        this.replay = replay;
    }

    public void sink(Observable<T> source) {
        replay.onNext(source);
    }

    // Good example why static create pattern is required
    public static <T> LazyReadOnlyField<T> create() {
        ReplaySubject<Observable<T>> replay = ReplaySubject.create();
        return new LazyReadOnlyField<>(replay);
    }
}
