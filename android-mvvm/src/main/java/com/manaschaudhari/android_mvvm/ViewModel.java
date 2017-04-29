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

public interface ViewModel {
    /**
     * Called when the {@link ViewModel} gets attached to the View.
     * Here the {@link ViewModel} can allocate resources for itself, that
     * are tied to the lifecycle of the View.
     **/
    void onViewAttached();

    /**
     * Called wheo the {@link ViewModel} gets detached from the View.
     * The ViewModel should free any resources that has been allocated
     * in {@link #onViewAttached()}.
     */
    void onViewDetached();

    /**
     * Called when the View is completely destroyed and the {@link ViewModel}
     * should also be destroyed. Here the {@link ViewModel} should free up
     * resources that has been allocated in the constructor.
     */
    void onDestroyed();
}