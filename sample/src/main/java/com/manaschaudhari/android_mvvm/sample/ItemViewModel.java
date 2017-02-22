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

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.adapters.MessageHelper;

import io.reactivex.functions.Action;

public class ItemViewModel implements ViewModel {
    @NonNull
    public final String name;

    public final @DrawableRes int imageRes;

    public boolean hasImage() {
        return imageRes != 0;
    }

    public ItemViewModel(@NonNull final Item item, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        this.imageRes = item.name.contains("2") ? R.drawable.some_image : 0;
        this.name = item.name.toUpperCase();
        this.onClicked = new Action() {
            @Override
            public void run() throws Exception {
                messageHelper.show("Selected " + item.name);
            }
        };
        this.onDetailsClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.openDetailsPage(item);
            }
        };
    }

    @Nullable
    public final Action onClicked;

    @Nullable
    public final Action onDetailsClicked;
}
