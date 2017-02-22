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

import com.manaschaudhari.android_mvvm.adapters.TestViewModel;
import com.manaschaudhari.android_mvvm.adapters.ViewProvider;

import java.util.List;

import io.reactivex.Observable;

public class BindingTestViewModel implements ViewModel {
    public List<TestViewModel> subclassList;
    public List<ViewModel> mixedList;
    public Observable<List<ViewModel>> observableMixedList;
    public Observable<List<TestViewModel>> observableSubclassMixedList;

    public ViewProvider viewProvider;
}
