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

package com.manaschaudhari.android_mvvm.adapters;

import io.reactivex.disposables.Disposable;

/**
 * A {@link com.manaschaudhari.android_mvvm.ViewModel} can implement this
 * interface to return a Subscription which will eventually be
 * unsubscribed from when the ViewModel's View is detached from the
 * view hierarchy.
 */
public interface Connectable {
    Disposable connect();
}
