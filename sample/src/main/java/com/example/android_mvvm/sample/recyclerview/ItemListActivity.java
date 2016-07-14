package com.example.android_mvvm.sample.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.adapters.RecyclerViewAdapter;
import com.example.android_mvvm.adapters.ViewModelBinder;
import com.example.android_mvvm.adapters.ViewProvider;
import com.example.android_mvvm.sample.R;
import com.example.android_mvvm.sample.BR;
import com.example.android_mvvm.sample.databinding.ActivityItemListBinding;

public class ItemListActivity extends AppCompatActivity {
    ItemListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ItemListViewModel();
        ActivityItemListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list);
        binding.setVm(viewModel);

        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(viewModel.itemVms, new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                return R.layout.row_item_with_image;
            }
        }, new ViewModelBinder() {
            @Override
            public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
                viewDataBinding.setVariable(BR.vm, viewModel);
            }
        });
        binding.rvItems.setAdapter(adapter);
    }
}
