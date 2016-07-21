package com.example.android_mvvm.sample.dagger;

import com.example.android_mvvm.sample.adapters.ItemListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ItemModule.class})
public interface ApplicationComponent {
    void inject(ItemListActivity activity);
}
