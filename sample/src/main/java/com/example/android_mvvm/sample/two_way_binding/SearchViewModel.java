package com.example.android_mvvm.sample.two_way_binding;

import android.databinding.ObservableField;

import com.example.android_mvvm.FieldUtils;
import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;

import rx.functions.Func1;

public class SearchViewModel implements ViewModel {
    public final ObservableField<String> searchQuery = new ObservableField<>("");

    public final ReadOnlyField<String> result = new ReadOnlyField<>(FieldUtils.toObservable(searchQuery).map(new Func1<String, String>() {
        @Override
        public String call(String s) {
            return s.toUpperCase();
        }
    }));
}
