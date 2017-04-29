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

import rx.functions.Action0;

public class ItemViewModel extends ViewModel {
    @NonNull
    public final String name;

    public final @DrawableRes int imageRes;

    public boolean hasImage() {
        return imageRes != 0;
    }

    public ItemViewModel(@NonNull final Item item, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        this.imageRes = item.name.contains("2") ? R.drawable.some_image : 0;
        this.name = item.name.toUpperCase();
        this.onClicked = new Action0() {
            @Override
            public void call() {
                messageHelper.show("Selected " + item.name);
            }
        };
        this.onDetailsClicked = new Action0() {
            @Override
            public void call() {
                navigator.openDetailsPage(item);
            }
        };
    }

    @Nullable
    public final Action0 onClicked;

    @Nullable
    public final Action0 onDetailsClicked;
}
