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

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TestViewModel implements ViewModel {
    int id;

    public TestViewModel(int id) {
        this.id = id;
    }

    @NonNull
    public static List<ViewModel> dummyViewModels(int n) {
        List<ViewModel> vms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vms.add(new TestViewModel(i));
        }
        return vms;
    }
}
