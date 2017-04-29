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

import android.support.annotation.CallSuper;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class for {@link ViewModel}. Useful to extend if you don't want to implement all methods.
 * <p>
 * This class also holds a {@link CompositeDisposable}, and in onDestroyed() all subscriptions will be cleared.
 */
public abstract class BaseViewModel implements ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onViewAttached() {
    }

    @Override
    @CallSuper
    public void onViewDetached() {
        disposable.clear();
    }

    @Override
    @CallSuper
    public void onDestroyed() {
        disposable.clear();
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }
}