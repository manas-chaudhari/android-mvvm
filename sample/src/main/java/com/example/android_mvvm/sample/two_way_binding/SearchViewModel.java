package com.example.android_mvvm.sample.two_way_binding;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.android_mvvm.FieldUtils;
import com.example.android_mvvm.ReadOnlyField;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemViewModel;
import com.example.android_mvvm.sample.Navigator;
import com.example.android_mvvm.sample.adapters.MessageHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class SearchViewModel implements ViewModel {
    public final ObservableField<String> searchQuery = new ObservableField<>("");

    public final ReadOnlyField<String> result = new ReadOnlyField<>(FieldUtils.toObservable(searchQuery).map(new Func1<String, String>() {
        @Override
        public String call(String s) {
            return s.toUpperCase();
        }
    }));

    public final Observable<List<ViewModel>> results;

    public SearchViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        results = FieldUtils.toObservable(searchQuery)
                .map(new Func1<String, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> call(String s) {
                        List<ViewModel> results = new ArrayList<>();
                        if (s.length() > 0) {
                            String[] words = s.split(" ");
                            for (String word : words) {
                                results.add(new ItemViewModel(new Item(word), messageHelper, navigator));
                            }
                        }
                        return results;
                    }
                });
    }
}
