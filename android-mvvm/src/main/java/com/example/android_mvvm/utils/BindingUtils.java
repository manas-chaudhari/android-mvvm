package com.example.android_mvvm.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android_mvvm.R;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.adapters.Connectable;
import com.example.android_mvvm.adapters.RecyclerViewAdapter;
import com.example.android_mvvm.adapters.ViewModelBinder;
import com.example.android_mvvm.adapters.ViewPagerAdapter;
import com.example.android_mvvm.adapters.ViewProvider;

import java.util.List;

import rx.Observable;
import rx.Subscription;

@SuppressWarnings("unused")
public class BindingUtils {

    private static ViewModelBinder defaultBinder = null;

    public static ViewModelBinder getDefaultBinder() {
        return defaultBinder;
    }

    public static void setDefaultBinder(ViewModelBinder viewModelBinder) {
        defaultBinder = viewModelBinder;
    }

    @BindingAdapter("adapter")
    public static void bindAdapter(ViewPager viewPager, PagerAdapter adapter) {
        PagerAdapter oldAdapter = viewPager.getAdapter();
        if (oldAdapter != null && oldAdapter instanceof Connectable) {
            Subscription subscription = (Subscription) viewPager.getTag(R.integer.tag_subscription);
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
        if (adapter != null && adapter instanceof Connectable) {
            viewPager.setTag(R.integer.tag_subscription, ((Connectable) adapter).connect());
        }
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter("adapter")
    public static void bindAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"items", "view_provider"})
    public static void bindAdapterWithDefaultBinder(RecyclerView recyclerView, Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        RecyclerViewAdapter adapter = null;
        if (items != null && viewProvider != null) {
            adapter = new RecyclerViewAdapter(items, viewProvider, defaultBinder);
        }
        bindAdapter(recyclerView, adapter);
    }

    @BindingAdapter({"items", "view_provider"})
    public static void bindAdapterWithDefaultBinder(ViewPager viewPager, Observable<List<ViewModel>> items, ViewProvider viewProvider) {
        ViewPagerAdapter adapter = null;
        if (items != null && viewProvider != null) {
            adapter = new ViewPagerAdapter(items, viewProvider, defaultBinder);
        }
        bindAdapter(viewPager, adapter);
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
}
