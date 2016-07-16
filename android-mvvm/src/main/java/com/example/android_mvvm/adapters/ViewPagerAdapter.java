package com.example.android_mvvm.adapters;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class ViewPagerAdapter extends PagerAdapter {

    @NonNull
    private List<ViewModel> latestViewModels = new ArrayList<>();

    @NonNull
    private final Observable<List<ViewModel>> source;

    @NonNull
    private final HashMap<DataSetObserver, Subscription> subscriptions = new HashMap<>();

    public ViewPagerAdapter(Observable<List<ViewModel>> viewModels, ViewProvider viewProvider, ViewModelBinder binder) {
        source = viewModels
                .doOnNext(new Action1<List<ViewModel>>() {
                    @Override
                    public void call(@Nullable List<ViewModel> viewModels) {
                        latestViewModels = (viewModels != null) ? viewModels : new ArrayList<ViewModel>();
                        notifyDataSetChanged();
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("ViewPagerAdapter", "Error in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<List<ViewModel>>empty())
                .share();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        subscriptions.put(observer, source.subscribe());
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
        Subscription subscription = subscriptions.remove(observer);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return latestViewModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
