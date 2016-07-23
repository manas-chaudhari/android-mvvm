package com.example.android_mvvm.sample;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;

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
