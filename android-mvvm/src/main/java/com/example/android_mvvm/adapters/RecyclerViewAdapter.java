package com.example.android_mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataBindingViewHolder> {
    private @NonNull List<ViewModel> latestViewModels = new ArrayList<>();
    private final @NonNull ViewProvider viewProvider;
    private final @NonNull ViewModelBinder binder;
    private final @NonNull Observable<List<ViewModel>> source;
    private final @NonNull HashMap<RecyclerView.AdapterDataObserver, Subscription> subscriptions = new HashMap<>();

    public RecyclerViewAdapter(@NonNull Observable<List<ViewModel>> viewModels,
                               @NonNull ViewProvider viewProvider,
                               @NonNull ViewModelBinder viewModelBinder) {
        this.viewProvider = viewProvider;
        this.binder = viewModelBinder;
        source = viewModels
                .doOnNext(new Action1<List<ViewModel>>() {
                    @Override
                    public void call(@Nullable List<ViewModel> viewModels) {
                        latestViewModels = viewModels != null ? viewModels : new ArrayList<ViewModel>();
                        notifyDataSetChanged();
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("RecyclerViewAdapter", "Error in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<List<ViewModel>>empty())
                .share();
    }

    @Override
    public int getItemViewType(int position) {
        return viewProvider.getView(latestViewModels.get(position));
    }

    @Override
    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new DataBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        binder.bind(holder.viewBinding, latestViewModels.get(position));
        holder.viewBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return latestViewModels.size();
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        subscriptions.put(observer, source.subscribe());
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        Subscription subscription = subscriptions.remove(observer);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public static class DataBindingViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding viewBinding;

        public DataBindingViewHolder(ViewDataBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
