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

package com.manaschaudhari.android_mvvm.rxjava;

import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ReadOnlyField;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Utilities for conversion between rx.Observable and databinding.ObservableField
 */
public class FieldUtils {
    @NonNull
    public static <T> rx.Observable<T> toObservable(@NonNull final ObservableField<T> field) {

        return RxJavaInterop.toV1Observable(Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> e) throws Exception {
                e.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        e.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        }), BackpressureStrategy.LATEST);
    }

    /**
     * A convenient wrapper for {@code ReadOnlyField#create(Observable)}
     * @return DataBinding field created from the specified Observable
     */
    @NonNull
    public static <T> ReadOnlyField<T> toField(@NonNull final rx.Observable<T> observable) {
        return ReadOnlyField.create(RxJavaInterop.toV2Observable(observable));
    }
}
