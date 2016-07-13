package com.example.android_mvvm.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ViewModel> latestViewModels = new ArrayList<>();

    public RecyclerViewAdapter(Observable<List<ViewModel>> viewModels) {
        viewModels.subscribe(new Action1<List<ViewModel>>() {
            @Override
            public void call(List<ViewModel> viewModels) {
                latestViewModels = viewModels;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return latestViewModels.size();
    }
}
