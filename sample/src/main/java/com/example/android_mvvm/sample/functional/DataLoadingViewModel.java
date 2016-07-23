package com.example.android_mvvm.sample.functional;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.utils.RxUtils;

import rx.Observable;
import rx.Single;

public class DataLoadingViewModel implements ViewModel {

    @NonNull
    public final ReadOnlyField<String> result;

    @NonNull
    public final ReadOnlyField<Boolean> progressVisible;

    public DataLoadingViewModel(@NonNull DataService service) {
        Single<String> data = service.loadData();
        final Observable<String> cachedData = data.toObservable().cache();

        Pair<Observable<String>, Observable<Boolean>> tracker = RxUtils.trackActivity(cachedData);

        result = new ReadOnlyField<>(tracker.first);
        this.progressVisible = new ReadOnlyField<>(tracker.second);
    }
}
