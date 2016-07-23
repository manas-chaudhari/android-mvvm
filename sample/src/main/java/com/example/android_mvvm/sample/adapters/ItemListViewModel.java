package com.example.android_mvvm.sample.adapters;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemViewModel;
import com.example.android_mvvm.sample.Navigator;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class ItemListViewModel {
    public final Observable<List<ViewModel>> itemVms;

    /**
     * Static non-terminating source will ensure that any non-closed subscription results in a memory leak
     */
    private static final Observable<List<Item>> itemsSource;
    static {
        List<Item> items = new ArrayList<>();

        items.add(new Item("item 1"));
        items.add(new Item("item 2"));
        items.add(new Item("item 3"));
        itemsSource = BehaviorSubject.create(items);
    }

    public ItemListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        this.itemVms = itemsSource.map(new Func1<List<Item>, List<ViewModel>>() {
            @Override
            public List<ViewModel> call(List<Item> items) {
                List<ViewModel> vms = new ArrayList<>();
                for (Item item : items) {
                    vms.add(new ItemViewModel(item, messageHelper, navigator));
                }
                return vms;
            }
        });
    }
}
