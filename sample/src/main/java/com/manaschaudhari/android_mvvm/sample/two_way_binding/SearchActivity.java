package com.manaschaudhari.android_mvvm.sample.two_way_binding;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.BaseActivity;
import com.manaschaudhari.android_mvvm.sample.R;

public class SearchActivity extends BaseActivity {

    @NonNull
    @Override
    public ViewModel createViewModel() {
        return new SearchViewModel(getMessageHelper(), getNavigator());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }
}
