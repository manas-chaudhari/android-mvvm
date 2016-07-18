package com.example.android_mvvm.sample.adapters;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemViewModel;
import com.example.android_mvvm.sample.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ItemListViewModel {
    public final Observable<List<ViewModel>> itemVms;

    /**
     * Static non-terminating source will ensure that any non-closed subscription results in a memory leak
     */
    private static final Observable<List<ViewModel>> itemViewModelSource;
    static {
        List<ViewModel> vms = new ArrayList<>();

        vms.add(new ItemViewModel(new Item("item 1"), 0));
        vms.add(new ItemViewModel(new Item("item 2"), R.drawable.some_image));
        vms.add(new ItemViewModel(new Item("item 1"), 0));
        itemViewModelSource = BehaviorSubject.create(vms);
    }

    public ItemListViewModel() {
        this.itemVms = itemViewModelSource;
    }
}
