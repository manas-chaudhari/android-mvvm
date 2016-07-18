package com.example.android_mvvm.sample;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.utils.BindingUtils;
import com.example.android_mvvm.adapters.ViewPagerAdapter;
import com.example.android_mvvm.sample.BR;
import com.example.android_mvvm.adapters.RecyclerViewAdapter;
import com.example.android_mvvm.adapters.ViewModelBinder;
import com.example.android_mvvm.adapters.ViewProvider;

import java.util.List;

import rx.Observable;

@SuppressWarnings("unused")
public class BindingAdapters {

    private static final ViewModelBinder defaultBinder = new ViewModelBinder() {
        @Override
        public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
            viewDataBinding.setVariable(BR.vm, viewModel);
        }
    };

    @BindingAdapter({"items", "view_provider"})
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        RecyclerViewAdapter adapter = null;
        if (items != null && viewProvider != null) {
            adapter = new RecyclerViewAdapter(items, viewProvider, defaultBinder);
        }
        RecyclerView.Adapter previousAdapter = recyclerView.getAdapter();
        recyclerView.setAdapter(adapter);

        // Previous adapter should get deallocated
        if (previousAdapter != null)
            ExampleApplication.getRefWatcher(recyclerView.getContext()).watch(previousAdapter);
    }

    @BindingConversion
    public static ViewProvider getViewProviderForStaticLayout(@LayoutRes final int layoutId) {
        return new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                return layoutId;
            }
        };
    }

    @BindingAdapter("layout_vertical")
    public static void bindLayoutManager(RecyclerView recyclerView, boolean vertical) {
        int orientation = vertical ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, false));
    }

    @BindingAdapter({"items", "view_provider"})
    public static void bindViewPagerAdapter(ViewPager viewPager, Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        ViewPagerAdapter adapter = null;
        if (items != null && viewProvider != null) {
            adapter = new ViewPagerAdapter(items, viewProvider, defaultBinder);
        }
        PagerAdapter previousAdapter = viewPager.getAdapter();
        BindingUtils.bindAdapter(viewPager, adapter);

        // Previous adapter should get deallocated
        if (previousAdapter != null)
            ExampleApplication.getRefWatcher(viewPager.getContext()).watch(previousAdapter);
    }
}
