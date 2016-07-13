package com.example.android_mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataBindingViewHolder> {
    private @NonNull List<ViewModel> latestViewModels = new ArrayList<>();
    private final @NonNull ViewProvider viewProvider;
    private final @NonNull ViewModelBinder binder;

    public RecyclerViewAdapter(@NonNull Observable<List<ViewModel>> viewModels,
                               @NonNull ViewProvider viewProvider,
                               @NonNull ViewModelBinder viewModelBinder) {
        this.viewProvider = viewProvider;
        this.binder = viewModelBinder;
        viewModels.subscribe(new Action1<List<ViewModel>>() {
            @Override
            public void call(List<ViewModel> viewModels) {
                latestViewModels = viewModels;
                notifyDataSetChanged();
            }
        });
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

    public static class DataBindingViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding viewBinding;

        public DataBindingViewHolder(ViewDataBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
