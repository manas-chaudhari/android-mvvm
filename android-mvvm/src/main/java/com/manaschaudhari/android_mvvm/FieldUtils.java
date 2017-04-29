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
import android.databinding.ObservableList;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;

public class FieldUtils {
    @NonNull
    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> field) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> subscriber) {
                if (field.get() != null)
                    subscriber.onNext(field.get());

                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        if (field.get() == null) {
                            Log.w("FieldUtils", "Null value received in ObservableField. Since Observables cannot emit null, ignoring this value.");
                            return;
                        }

                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);

                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }

    @NonNull
    public static Observable<Integer> toObservable(@NonNull final ObservableInt field) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);

                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }

    @NonNull
    public static Observable<Boolean> toObservable(@NonNull final ObservableBoolean field) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }

    @NonNull
    public static Observable<Float> toObservable(@NonNull final ObservableFloat field) {
        return Observable.create(new ObservableOnSubscribe<Float>() {
            @Override
            public void subscribe(final ObservableEmitter<Float> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }

    @NonNull
    public static Observable<Double> toObservable(@NonNull final ObservableDouble field) {
        return Observable.create(new ObservableOnSubscribe<Double>() {
            @Override
            public void subscribe(final ObservableEmitter<Double> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }

    @NonNull
    public static Observable<Long> toObservable(@NonNull final ObservableLong field) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> subscriber) {
                subscriber.onNext(field.get());
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                });
            }
        });
    }

    @NonNull
    public static <T> Observable<List<T>> toObservable(@NonNull final ObservableList<T> field) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<T>> subscriber) {
                subscriber.onNext(field);
                final ObservableList.OnListChangedCallback<ObservableList<T>> callback = new ObservableList.OnListChangedCallback<ObservableList<T>>() {
                    @Override
                    public void onChanged(ObservableList observableList) {
                        subscriber.onNext(field);
                    }

                    @Override
                    public void onItemRangeChanged(ObservableList observableList, int i, int i1) {
                        onChanged(observableList);
                    }

                    @Override
                    public void onItemRangeInserted(ObservableList observableList, int i, int i1) {
                        onChanged(observableList);
                    }

                    @Override
                    public void onItemRangeMoved(ObservableList observableList, int i, int i1, int i2) {
                        onChanged(observableList);
                    }

                    @Override
                    public void onItemRangeRemoved(ObservableList observableList, int i, int i1) {
                        onChanged(observableList);
                    }
                };
                field.addOnListChangedCallback(callback);
                subscriber.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        field.removeOnListChangedCallback(callback);
                    }
                });
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

    @NonNull
    public static <T> Disposable bindTo(@NonNull final Observable<T> observable, @NonNull final ObservableField<T> field) {
        return observable.subscribe(new Consumer<T>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull T t) throws Exception {
                field.set(t);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.e("FieldUtils", "onError in source observable", throwable);
            }
        });
    }

/*    @NonNull
    public static <T> Disposable bindTo(@NonNull final ObservableSource<T> observable, @NonNull final Observer<T> observer) {
        observable.subscribe(observer);
        return observable.subscribe(new Consumer<T>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull T t) throws Exception {
                field.onNext(t);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.e("FieldUtils", "onError in source observable", throwable);
            }
        });
    }*/

    @NonNull
    public static <T extends List<U>, U> ReadOnlyList<T, U> toList(@NonNull final Observable<T> observable) {
        return ReadOnlyList.create(observable);
    }
}