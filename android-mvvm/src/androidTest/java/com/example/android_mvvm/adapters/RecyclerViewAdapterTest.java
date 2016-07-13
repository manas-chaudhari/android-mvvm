package com.example.android_mvvm.adapters;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.android_mvvm.ViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.BehaviorSubject;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewAdapterTest {

    public static final int INITIAL_COUNT = 3;
    private BehaviorSubject<List<ViewModel>> viewModelsSource;
    private RecyclerViewAdapter sut;
    private int notifyCallCount;

    @Before
    public void setUp() throws Exception {
        List<ViewModel> vms = dummyViewModels(INITIAL_COUNT);

        viewModelsSource = BehaviorSubject.create(vms);
        sut = new RecyclerViewAdapter(viewModelsSource);

        notifyCallCount = 0;
        sut.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyCallCount++;
            }
        });
    }

    @Test
    public void initialItemCount() throws Exception {
        assertEquals(INITIAL_COUNT, sut.getItemCount());
    }

    @Test
    public void itemCountOnUpdate() throws Exception {
        viewModelsSource.onNext(dummyViewModels(10));

        assertEquals(10, sut.getItemCount());
    }

    @Test
    public void notifyIsCalledOnUpdate() throws Exception {
        viewModelsSource.onNext(dummyViewModels(4));

        assertEquals(1, notifyCallCount);
    }

    @NonNull
    private List<ViewModel> dummyViewModels(int n) {
        List<ViewModel> vms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vms.add(new TestViewModel(i));
        }
        return vms;
    }

    public class TestViewModel implements ViewModel {
        int id;
        public TestViewModel(int id) {
            this.id = id;
        }
    }
}