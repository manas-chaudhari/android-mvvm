package com.example.android_mvvm.sample.adapters;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.BaseActivity;
import com.example.android_mvvm.sample.R;

public class ItemListActivity extends BaseActivity {
    @NonNull
    @Override
    public ViewModel createViewModel() {
        return new ItemListViewModel(new MessageHelper() {
            @Override
            public void show(String message) {
                Toast.makeText(ItemListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }, getNavigator());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_item_list;
    }
}
