package com.example.android_mvvm.sample.two_way_binding;

import android.support.annotation.NonNull;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.BaseActivity;
import com.example.android_mvvm.sample.R;

public class SearchActivity extends BaseActivity {

    @NonNull
    @Override
    public ViewModel createViewModel() {
        return new SearchViewModel();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }
}
