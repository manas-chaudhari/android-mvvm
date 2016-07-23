package com.example.android_mvvm.sample.functional;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.BaseActivity;
import com.example.android_mvvm.sample.R;

public class DataLoadingActivity extends BaseActivity {

    @NonNull
    @Override
    public ViewModel createViewModel() {
        return new DataLoadingViewModel(new DataService());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_loading;
    }
}
