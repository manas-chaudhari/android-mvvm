package com.example.android_mvvm.sample.adapters;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android_mvvm.sample.R;
import com.example.android_mvvm.sample.dagger.ActivityComponent;
import com.example.android_mvvm.sample.dagger.ActivityModule;
import com.example.android_mvvm.sample.dagger.DaggerActivityComponent;
import com.example.android_mvvm.sample.databinding.ActivityItemListBinding;

import javax.inject.Inject;

public class ItemListActivity extends AppCompatActivity {
    @Inject
    ItemListViewModel viewModel;
    private ActivityItemListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build();
        activityComponent.inject(this);
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
