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
import com.manaschaudhari.android_mvvm.adapters.ViewProvider;

public class ViewProviders {

    @NonNull
    public static ViewProvider getItemListing() {
        return new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                if (vm instanceof ItemViewModel) {
                    return ((ItemViewModel) vm).hasImage() ? R.layout.row_item_with_image : R.layout.row_item_without_image;
                }
                return 0;
            }
        };
    }
}
