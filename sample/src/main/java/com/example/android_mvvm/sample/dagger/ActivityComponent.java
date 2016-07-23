package com.example.android_mvvm.sample.dagger;

import com.example.android_mvvm.sample.adapters.ItemListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ActivityModule.class, ItemModule.class})
public interface ActivityComponent {
    void inject(ItemListActivity activity);
}
