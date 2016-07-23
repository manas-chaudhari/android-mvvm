package com.example.android_mvvm.sample.dagger;

import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemViewModel;
import com.example.android_mvvm.sample.Navigator;
import com.example.android_mvvm.sample.MessageHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@Module()
@Singleton
public class ItemModule {
    @Provides
    Func1<Item, ItemViewModel> provideItemVMGenerator(final MessageHelper messageHelper, final Navigator navigator) {
        return new Func1<Item, ItemViewModel>() {
            @Override
            public ItemViewModel call(Item item) {
                return new ItemViewModel(item, messageHelper, navigator);
            }
        };
    }
}
