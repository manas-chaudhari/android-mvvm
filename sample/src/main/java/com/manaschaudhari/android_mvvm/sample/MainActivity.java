package com.manaschaudhari.android_mvvm.sample;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;

public class MainActivity extends BaseActivity {

    @NonNull
    @Override
    public ViewModel createViewModel() {
        return new MainViewModel(getNavigator());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
