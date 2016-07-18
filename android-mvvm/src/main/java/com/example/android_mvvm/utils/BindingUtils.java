package com.example.android_mvvm.utils;

import android.databinding.BindingAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.android_mvvm.R;
import com.example.android_mvvm.adapters.Connectable;

import rx.Subscription;

public class BindingUtils {
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
}
