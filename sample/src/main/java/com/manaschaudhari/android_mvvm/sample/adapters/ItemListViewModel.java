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

package com.manaschaudhari.android_mvvm.sample.adapters;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.Item;
import com.manaschaudhari.android_mvvm.sample.ItemViewModel;
import com.manaschaudhari.android_mvvm.sample.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class ItemListViewModel implements ViewModel {
    public final Observable<List<ItemViewModel>> itemVms;

    /**
     * Static non-terminating source will ensure that any non-closed subscription results in a memory leak
     */
    private static final Observable<List<Item>> itemsSource;

    static {
        List<Item> items = new ArrayList<>();

        items.add(new Item("item 1"));
        items.add(new Item("item 2"));
        items.add(new Item("item 3"));
        itemsSource = BehaviorSubject.createDefault(items);
    }

    public ItemListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        this.itemVms = itemsSource.map(new Function<List<Item>, List<ItemViewModel>>() {
            @Override
            public List<ItemViewModel> apply(List<Item> items) throws Exception {
                List<ItemViewModel> vms = new ArrayList<>();
                for (Item item : items) {
                    vms.add(new ItemViewModel(item, messageHelper, navigator));
                }
                return vms;
            }
        });
    }
}
