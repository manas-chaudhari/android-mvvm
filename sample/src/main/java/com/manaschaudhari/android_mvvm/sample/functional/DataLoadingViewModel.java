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

import com.manaschaudhari.android_mvvm.ReadOnlyField;
import com.manaschaudhari.android_mvvm.ViewModel;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

import static com.manaschaudhari.android_mvvm.FieldUtils.toField;

public class DataLoadingViewModel implements ViewModel {
    @NonNull
    public final ReadOnlyField<String> result;

    @NonNull
    public final ReadOnlyField<Boolean> progressVisible;

    @NonNull
    public final ReadOnlyField<Boolean> errorVisible;

    @NonNull
    public final ReadOnlyField<Boolean> resultVisible;

    public DataLoadingViewModel(@NonNull DataService service) {
        Single<Result<String>> data = service.loadData();
        final Observable<Result<String>> cachedData = data.toObservable().startWith(Result.<String>empty()).cache();

        result = toField(cachedData.map(new Function<Result<String>, String>() {
            @Override
            public String apply(@io.reactivex.annotations.NonNull Result<String> stringResult) throws Exception {
                return stringResult.isSuccess() ? stringResult.value : "";
            }
        }));
        this.progressVisible = toField(cachedData.map(new Function<Result<String>, Boolean>() {
            @Override
            public Boolean apply(@io.reactivex.annotations.NonNull Result<String> stringResult) throws Exception {
                return stringResult.isEmpty();
            }
        }));
        this.resultVisible = toField(cachedData.map(new Function<Result<String>, Boolean>() {
            @Override
            public Boolean apply(@io.reactivex.annotations.NonNull Result<String> stringResult) throws Exception {
                return stringResult.isSuccess();
            }
        }));
        this.errorVisible = toField(cachedData.map(new Function<Result<String>, Boolean>() {
            @Override
            public Boolean apply(@io.reactivex.annotations.NonNull Result<String> stringResult) throws Exception {
                return stringResult.isError();
            }
        }));
    }
}
