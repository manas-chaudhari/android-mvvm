/*
 * Copyright 2016 Manas Chaudhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manaschaudhari.android_mvvm.sample.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class RxUtils {
    @NonNull
    public static <T> Pair<Observable<T>, Observable<Boolean>> trackActivity(@NonNull final Observable<T> source) {
        final BehaviorSubject<Integer> count = BehaviorSubject.createDefault(0);
        return new Pair<>(
                Observable.using(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        count.onNext(count.getValue() + 1);
                        return null;
                    }
                }, new Function<Integer, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(Integer integer) throws Exception {
                        return source;
                    }
                }, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        count.onNext(count.getValue() - 1);
                    }
                }),
                count.map(new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer) throws Exception {
                        return integer > 0;
                    }
                }));
    }
}
