package com.example.android_mvvm.sample.functional;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.utils.RxUtils;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.functions.Func2;

public class DataLoadingViewModel implements ViewModel {

    @NonNull
    public final ReadOnlyField<String> result;

    @NonNull
    public final ReadOnlyField<Boolean> progressVisible;

    @NonNull
    public final ReadOnlyField<Boolean> errorVisible;

    public DataLoadingViewModel(@NonNull DataService service) {
        Single<String> data = service.loadData(); // Try service.loadData_Fail(); for error scenario
        final Observable<String> cachedData = data.toObservable().onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return null;
            }
        }).cache();

        Pair<Observable<String>, Observable<Boolean>> tracker = RxUtils.trackActivity(cachedData);

        result = new ReadOnlyField<>(tracker.first);
        this.progressVisible = new ReadOnlyField<>(tracker.second);
        this.errorVisible = new ReadOnlyField<>(Observable.combineLatest(tracker.first, tracker.second, new Func2<String, Boolean, Boolean>() {
            @Override
            public Boolean call(String result, Boolean inProgress) {
                return !inProgress && result == null;
            }
        }));
    }
}
