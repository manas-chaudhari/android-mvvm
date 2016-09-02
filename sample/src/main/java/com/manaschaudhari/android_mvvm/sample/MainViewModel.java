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

package com.manaschaudhari.android_mvvm.sample;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;

import rx.functions.Action0;

public class MainViewModel implements ViewModel {
    @NonNull
    private final Navigator navigator;

    public MainViewModel(@NonNull Navigator navigator) {

        this.navigator = navigator;
    }

    public final Action0 onAdapterClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToAdapterDemo();
        }
    };

    public final Action0 onFunctionalClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToFunctionalDemo();
        }
    };

    public final Action0 onTwoWayBindingClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToTwoWayBindingDemo();
        }
    };

    public final Action0 onCalculatorExampleClick = new Action0() {
        @Override
        public void call() {
            navigator.navigateToCalculatorDemo();
        }
    };
}
