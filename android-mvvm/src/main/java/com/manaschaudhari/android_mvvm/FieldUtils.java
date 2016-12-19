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

package com.manaschaudhari.android_mvvm;

import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class FieldUtils {
    @NonNull
    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> field) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }

    @NonNull
    public static Observable<Integer> toObservable(@NonNull final ObservableInt field) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }

    @NonNull
    public static Observable<Boolean> toObservable(@NonNull final ObservableBoolean field) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }

    @NonNull
    public static Observable<Float> toObservable(@NonNull final ObservableFloat field) {
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(final Subscriber<? super Float> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }

    @NonNull
    public static Observable<Double> toObservable(@NonNull final ObservableDouble field) {
        return Observable.create(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(final Subscriber<? super Double> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }

    @NonNull
    public static Observable<Long> toObservable(@NonNull final ObservableLong field) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(final Subscriber<? super Long> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }

    /**
     * A convenient wrapper for {@code ReadOnlyField#create(Observable)}
     *
     * @return DataBinding field created from the specified Observable
     */
    @NonNull
    public static <T> ReadOnlyField<T> toField(@NonNull final Observable<T> observable) {
        return ReadOnlyField.create(observable);
    }
}
