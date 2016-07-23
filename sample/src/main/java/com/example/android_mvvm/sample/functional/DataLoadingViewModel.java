package com.example.android_mvvm.sample.functional;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;

import rx.Observable;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class DataLoadingViewModel implements ViewModel {

    @NonNull
    public final ReadOnlyField<String> result;

    @NonNull
    public final ReadOnlyField<Boolean> progressVisible;

    public DataLoadingViewModel(@NonNull DataService service) {
        final BehaviorSubject<Integer> count = BehaviorSubject.create(0);
        Single<String> data = service.loadData();
        final Observable<String> cachedData = data.toObservable().cache();
        result = new ReadOnlyField<>(Observable.using(new Func0<Integer>() {
            @Override
            public Integer call() {
                count.onNext(count.getValue() + 1);
                return null;
            }
        }, new Func1<Integer, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Integer integer) {
                return cachedData;
            }
        }, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                count.onNext(count.getValue() - 1);
            }
        }));
        this.progressVisible = new ReadOnlyField<>(count.map(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer > 0;
            }
        }));
    }
}
