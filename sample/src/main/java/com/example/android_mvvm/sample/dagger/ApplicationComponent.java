package com.example.android_mvvm.sample.dagger;

import com.example.android_mvvm.sample.adapters.ItemListActivity;

import dagger.Component;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(ItemListActivity activity);
}
