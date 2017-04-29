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

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReadOnlyList<T extends List<U>, U> extends ObservableArrayList<U> {
    final Observable<T> source;
    private Disposable subscription;
    private int listenerCount = 0;

    public static <E extends List<X>, X> ReadOnlyList<E, X> create(@NonNull Observable<E> source) {
        return new ReadOnlyList<>(source);
    }

    protected ReadOnlyList(@NonNull Observable<T> source) {
        super();
        this.source = source;
        subscribe();
    }

    /**
     * @deprecated Setter of ReadOnlyField does nothing. Merge with the source Observable instead.
     * See <a href="https://github.com/manas-chaudhari/android-mvvm/tree/master/Documentation/ObservablesAndSetters.md">Documentation/ObservablesAndSetters.md</a>
     */
    @Override
    public boolean add(U object) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean addAll(Collection<? extends U> collection) {
        return false;
    }

    @Override
    public void addOnListChangedCallback(OnListChangedCallback listener) {
        super.addOnListChangedCallback(listener);
        listenerCount++;
        if (subscription == null || subscription.isDisposed()) {
            subscribe();
        }
    }

    private void subscribe() {
        subscription = this.source.subscribe(new Consumer<T>() {
            @Override
            public void accept(T us) {
                ReadOnlyList.super.clear();
                ReadOnlyList.super.addAll(us);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                Log.e("ReadOnlyList", "onError in source observable", throwable);
            }
        });
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback listener) {
        super.removeOnListChangedCallback(listener);
        if (--listenerCount == 0 && subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
            subscription = null;
        }
    }
}
