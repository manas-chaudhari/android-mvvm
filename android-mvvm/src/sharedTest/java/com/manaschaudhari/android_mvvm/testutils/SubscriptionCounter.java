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

package com.manaschaudhari.android_mvvm.testutils;

import rx.Observable;
import rx.functions.Action0;

public class SubscriptionCounter<T> implements Observable.Transformer<T, T> {
    public int subscriptions;
    public int unsubscriptions;

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                subscriptions++;
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                unsubscriptions++;
            }
        });
    }
}
