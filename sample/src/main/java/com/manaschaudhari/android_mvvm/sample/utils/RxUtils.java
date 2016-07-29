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
