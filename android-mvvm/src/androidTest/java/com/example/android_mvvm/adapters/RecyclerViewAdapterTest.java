package com.example.android_mvvm.adapters;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.testutils.SubscriptionCounter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewAdapterTest {

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    public static final int INITIAL_COUNT = 3;
    private BehaviorSubject<List<ViewModel>> viewModelsSource;
    private RecyclerViewAdapter sut;
    private int notifyCallCount;
    private TestViewModelBinder testBinder;
    private ViewProvider testViewProvider;
    private SubscriptionCounter<List<ViewModel>> subscriptionCounter;
    private RecyclerView.AdapterDataObserver defaultObserver;

    @Before
    public void setUp() throws Exception {
        List<ViewModel> vms = dummyViewModels(INITIAL_COUNT);

        viewModelsSource = BehaviorSubject.create(vms);
        testViewProvider = new TestViewProvider();
        testBinder = new TestViewModelBinder();
        subscriptionCounter = new SubscriptionCounter<>();
        sut = new RecyclerViewAdapter(viewModelsSource.compose(subscriptionCounter),
                testViewProvider, testBinder);

        notifyCallCount = 0;
        defaultObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyCallCount++;
            }
        };
        sut.registerAdapterDataObserver(defaultObserver);
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
    public void noSubscriptionsInitially() throws Exception {
        SubscriptionCounter<List<ViewModel>> counter = new SubscriptionCounter<>();
        Observable<List<ViewModel>> source = viewModelsSource.compose(counter);

        RecyclerViewAdapter sut = new RecyclerViewAdapter(source, testViewProvider, testBinder);

        assertEquals(0, counter.subscriptions);
    }

    @Test
    public void noSubscriptionsAfterRemovingAdapterObserver() throws Exception {
        // defaultObserver is added in setup to measure notifyCount
        assertEquals(1, subscriptionCounter.subscriptions);
        assertEquals(0, subscriptionCounter.unsubscriptions);

        sut.unregisterAdapterDataObserver(defaultObserver);

        assertEquals(0, subscriptionCounter.subscriptions - subscriptionCounter.unsubscriptions);
    }

    @Test
    public void nullListIsTreatedAsEmpty() throws Exception {
        viewModelsSource.onNext(null);

        assertEquals(0, sut.getItemCount());
    }

    @Test
    public void errorIsHandled() throws Exception {
        viewModelsSource.onError(new Throwable());

        assertEquals(INITIAL_COUNT, sut.getItemCount());
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

    @Test
    @UiThreadTest
    public void bindViewHolder() throws Exception {
        ViewGroup view = new LinearLayout(InstrumentationRegistry.getContext());
        TestViewDataBinding binding = new TestViewDataBinding(view);
        sut.onBindViewHolder(new RecyclerViewAdapter.DataBindingViewHolder(binding), 0);

        assertTrue(testBinder.lastBinding == binding);
        assertTrue(testBinder.lastViewModel == viewModelsSource.getValue().get(0));
        assertEquals(1, binding.executePendingBindingsCallCount);
    }

    @NonNull
    public static List<ViewModel> dummyViewModels(int n) {
        List<ViewModel> vms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            vms.add(new TestViewModel(i));
        }
        return vms;
    }

    public static class TestViewModel implements ViewModel {
        int id;

        public TestViewModel(int id) {
            this.id = id;
        }
    }

    public static class TestViewProvider implements ViewProvider {

        @Override
        public int getView(ViewModel vm) {
            if (vm instanceof TestViewModel) {
                return ((TestViewModel) vm).id;
            }
            return 0;
        }
    }

    public static class TestViewDataBinding extends ViewDataBinding {
        int executePendingBindingsCallCount = 0;

        protected TestViewDataBinding(View root) {
            super(null, root, 1);
        }

        @Override
        protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
            return false;
        }

        @Override
        public boolean setVariable(int variableId, Object value) {
            return false;
        }

        @Override
        protected void executeBindings() {}

        @Override
        public void executePendingBindings() {
            executePendingBindingsCallCount++;
        }

        @Override
        public void invalidateAll() {

        }

        @Override
        public boolean hasPendingBindings() {
            return false;
        }
    }

    public static class TestViewModelBinder implements ViewModelBinder{
        ViewDataBinding lastBinding;
        ViewModel lastViewModel;

        @Override
        public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
            lastBinding = viewDataBinding;
            lastViewModel = viewModel;
        }
    }
}