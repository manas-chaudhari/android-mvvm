package com.example.android_mvvm.adapters;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android_mvvm.ViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.BehaviorSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewAdapterTest {

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    public static final int INITIAL_COUNT = 3;
    private BehaviorSubject<List<ViewModel>> viewModelsSource;
    private RecyclerViewAdapter sut;
    private int notifyCallCount;
    private ViewProvider testViewProvider;

    @Before
    public void setUp() throws Exception {
        List<ViewModel> vms = dummyViewModels(INITIAL_COUNT);

        viewModelsSource = BehaviorSubject.create(vms);
        testViewProvider = new TestViewProvider();
        sut = new RecyclerViewAdapter(viewModelsSource, testViewProvider);

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

    @Test
    public void itemTypeIsBasedOnViewProvider() throws Exception {
        List<ViewModel> vms = dummyViewModels(4);
        vms.remove(1);
        viewModelsSource.onNext(vms);

        assertEquals(0, sut.getItemViewType(0));
        assertEquals(2, sut.getItemViewType(1));
        assertEquals(3, sut.getItemViewType(2));
    }


    @Test
    @UiThreadTest
    public void createViewHolder() throws Exception {
        ViewGroup parent = new LinearLayout(InstrumentationRegistry.getContext());
        RecyclerViewAdapter.DataBindingViewHolder holder = sut.onCreateViewHolder(parent, com.example.android_mvvm.test.R.layout.layout_test);

        assertNotNull(holder);
        // Class isn't available at compile time
        assertEquals("com.example.android_mvvm.test.databinding.LayoutTestBinding", holder.viewBinding.getClass().getName());
    }

    // TODO: Test for binding
    // TODO: Test no subscribers after unregistering
    // TODO: Test Errors, null

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

    public class TestViewProvider implements ViewProvider {

        @Override
        public int getView(ViewModel vm) {
            if (vm instanceof TestViewModel) {
                return ((TestViewModel) vm).id;
            }
            return 0;
        }
    }
}