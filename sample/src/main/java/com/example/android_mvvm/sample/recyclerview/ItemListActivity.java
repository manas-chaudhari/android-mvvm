package com.example.android_mvvm.sample.recyclerview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android_mvvm.sample.R;
import com.example.android_mvvm.sample.databinding.ActivityItemListBinding;

public class ItemListActivity extends AppCompatActivity {
    ItemListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ItemListViewModel();
        ActivityItemListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list);
        binding.setVm(viewModel);
    }
}
