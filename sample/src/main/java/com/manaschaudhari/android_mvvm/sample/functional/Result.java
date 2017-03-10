/*
 * Copyright 2017 Manas Chaudhari
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

public class Result<T> {
    public final T value;
    public final Throwable error;

    private Result(T value, Throwable error) {
        this.value = value;
        this.error = error;
        if (value != null && error != null) {
            throw new IllegalStateException("Both value and error cannot be NonNull");
        }
    }

    public boolean isEmpty() {
        return error == null && value == null;
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isSuccess() {
        return value != null;
    }

    public static <T> Result<T> empty() {
        return new Result<>(null, null);
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> error(Throwable error) {
        return new Result<>(null, error);
    }
}
