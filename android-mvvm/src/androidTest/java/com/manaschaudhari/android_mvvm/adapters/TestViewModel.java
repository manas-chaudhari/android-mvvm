package com.manaschaudhari.android_mvvm.adapters;

import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TestViewModel implements ViewModel {
    int id;

    public TestViewModel(int id) {
        this.id = id;
    }

    @NonNull
    public static List<ViewModel> dummyViewModels(int n) {
        List<ViewModel> vms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vms.add(new TestViewModel(i));
        }
        return vms;
    }
}
