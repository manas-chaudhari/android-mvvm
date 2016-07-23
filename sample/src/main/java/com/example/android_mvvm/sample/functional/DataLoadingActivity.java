package com.example.android_mvvm.sample.functional;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android_mvvm.sample.R;
import com.example.android_mvvm.sample.databinding.ActivityDataLoadingBinding;

public class DataLoadingActivity extends AppCompatActivity {

    private ActivityDataLoadingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_loading);
        DataLoadingViewModel vm = new DataLoadingViewModel(new DataService());
        binding.setVm(vm);
    }

    @Override
    protected void onDestroy() {
        binding.setVm(null);
        binding.executePendingBindings();
        super.onDestroy();
    }
}
