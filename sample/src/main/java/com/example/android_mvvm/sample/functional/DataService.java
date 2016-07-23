package com.example.android_mvvm.sample.functional;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataService {
    public Single<String> loadData() {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                singleSubscriber.onSuccess("Result from data service");
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
}
