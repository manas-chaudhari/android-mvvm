package com.manaschaudhari.android_mvvm.sample.two_way_binding;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.FieldUtils;
import com.manaschaudhari.android_mvvm.ReadOnlyField;
import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.Item;
import com.manaschaudhari.android_mvvm.sample.ItemViewModel;
import com.manaschaudhari.android_mvvm.sample.Navigator;
import com.manaschaudhari.android_mvvm.sample.adapters.MessageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class SearchViewModel implements ViewModel {
    public final ObservableField<String> searchQuery = new ObservableField<>("");
    public final Observable<List<ViewModel>> results;
    public final Action0 onRandomSearch;

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

        onRandomSearch = new Action0() {
            @Override
            public void call() {
                String randomString = UUID.randomUUID().toString();
                String randomQuery = randomString.substring(0, 3) + " " + randomString.substring(5, 8);
                searchQuery.set(randomQuery);
            }
        };
    }
}
