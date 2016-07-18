package com.example.android_mvvm.sample.adapters;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemViewModel;
import com.example.android_mvvm.sample.R;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class ItemListViewModel {
    public final Observable<List<ViewModel>> itemVms;

    public ItemListViewModel() {
        this.itemVms = Observable.just("item 1", "item 2", "item 3")
                .map(new Func1<String, ViewModel>() {
                    @Override
                    public ViewModel call(String name) {
                        int image = name.contains("2") ? R.drawable.some_image : 0;
                        return new ItemViewModel(new Item(name), image);
                    }
                })
                .toList();
    }
}
