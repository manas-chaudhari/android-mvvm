package com.example.android_mvvm.sample.recyclerview;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class ItemListViewModel {
    public final Observable<List<ViewModel>> itemVms;

    public ItemListViewModel() {
        this.itemVms = Observable.just("item 1", "item 2", "item 3")
                .map(new Func1<String, Item>() {
                    @Override
                    public Item call(String s) {
                        return new Item(s);
                    }
                })
                .map(new Func1<Item, ViewModel>() {
                    @Override
                    public ViewModel call(Item item) {
                        return new ItemViewModel(item);
                    }
                })
                .toList();
    }
}
