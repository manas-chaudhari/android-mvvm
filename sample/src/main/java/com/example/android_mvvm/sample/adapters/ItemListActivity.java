package com.example.android_mvvm.sample.adapters;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android_mvvm.sample.R;
import com.example.android_mvvm.sample.databinding.ActivityItemListBinding;

public class ItemListActivity extends AppCompatActivity {
    ItemListViewModel viewModel;
    private ActivityItemListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ItemListViewModel(new ShowMessage() {
            @Override
            public void show(String message) {
                Toast.makeText(ItemListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
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
