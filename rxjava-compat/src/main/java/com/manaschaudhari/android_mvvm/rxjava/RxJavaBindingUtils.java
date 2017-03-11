package com.manaschaudhari.android_mvvm.rxjava;

import android.databinding.BindingConversion;
import android.support.annotation.Nullable;

import com.manaschaudhari.android_mvvm.ViewModel;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.functions.Function;
import rx.Observable;

public class RxJavaBindingUtils {
    @BindingConversion
    @Nullable
    public static <U extends ViewModel> io.reactivex.Observable<List<ViewModel>> toV1GenericList(@Nullable Observable<List<U>> specificList) {
        return specificList == null ? null : RxJavaInterop.toV2Observable(specificList).map(new Function<List<U>, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(List<U> ts) throws Exception {
                return new ArrayList<ViewModel>(ts);
            }
        });
    }
}
