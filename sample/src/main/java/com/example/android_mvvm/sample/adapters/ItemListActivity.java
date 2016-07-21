package com.example.android_mvvm.sample.adapters;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android_mvvm.sample.ExampleApplication;
import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemDetailsActivity;
import com.example.android_mvvm.sample.Navigator;
import com.example.android_mvvm.sample.R;
import com.example.android_mvvm.sample.databinding.ActivityItemListBinding;

import javax.inject.Inject;

public class ItemListActivity extends AppCompatActivity {
    @Inject
    ItemListViewModel viewModel;
    private ActivityItemListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExampleApplication.getApplicationComponent(this).inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list);
        binding.setVm(viewModel);
        setTitle("Adapters Demo");
    }

    @Override
    protected void onDestroy() {
        binding.setVm(null);
        binding.executePendingBindings();
        super.onDestroy();
    }
}
