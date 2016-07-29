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

package com.manaschaudhari.android_mvvm.sample.functional;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.manaschaudhari.android_mvvm.ReadOnlyField;
import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.utils.RxUtils;

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
