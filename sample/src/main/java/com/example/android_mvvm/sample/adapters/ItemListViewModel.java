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
import rx.subjects.PublishSubject;

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

    public ItemListViewModel(@NonNull final ShowMessage showMessage, @NonNull final Navigator navigator) {
        this.itemVms = itemsSource.map(new Func1<List<Item>, List<ViewModel>>() {
            @Override
            public List<ViewModel> call(List<Item> items) {
                List<ItemViewModel> vms = new ArrayList<>();
                List<Observable<ItemViewModel>> vmClickedList = new ArrayList<>();
                for (Item item : items) {
                    final ItemViewModel vm = new ItemViewModel(item, showMessage, navigator);
                    vms.add(vm);
                    vmClickedList.add(vm.onClicked.map(new Func1<Void, ItemViewModel>() {
                        @Override
                        public ItemViewModel call(Void aVoid) {
                            return vm;
                        }
                    }));
                }
                Observable<ItemViewModel> vmClicked = Observable.merge(vmClickedList);
                for (final ItemViewModel vm : vms) {
                    vm.setSelected(vmClicked.map(new Func1<ItemViewModel, Boolean>() {
                        @Override
                        public Boolean call(ItemViewModel itemViewModel) {
                            return itemViewModel == vm;
                        }
                    }));
                }

                return new ArrayList<ViewModel>(vms);
            }
        }).cache();
    }
}
