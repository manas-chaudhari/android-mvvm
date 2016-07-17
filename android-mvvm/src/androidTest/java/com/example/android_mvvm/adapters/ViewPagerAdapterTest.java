package com.example.android_mvvm.adapters;

import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android_mvvm.test.R;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.testutils.SubscriptionCounter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ViewPagerAdapterTest {

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    public static final int INITIAL_COUNT = 3;
    private ViewPagerAdapter sut;
    private BehaviorSubject<List<ViewModel>> viewModelsSource;
    private SubscriptionCounter<List<ViewModel>> subscriptionCounter;
    private TestViewProvider testViewProvider;
    private RecyclerViewAdapterTest.TestViewModelBinder testBinder;
    private int notifyCallCount;
    private DataSetObserver defaultObserver;

    @Before
    public void setUp() throws Exception {
        viewModelsSource = BehaviorSubject.create(RecyclerViewAdapterTest.dummyViewModels(INITIAL_COUNT));
        testViewProvider = new TestViewProvider();
        testBinder = new RecyclerViewAdapterTest.TestViewModelBinder();
        subscriptionCounter = new SubscriptionCounter<>();
        sut = new ViewPagerAdapter(viewModelsSource.compose(subscriptionCounter),
                testViewProvider, testBinder);

        notifyCallCount = 0;
        defaultObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyCallCount++;
            }
        };
        sut.registerDataSetObserver(defaultObserver);
    }

    @Test
    public void initialItemCount() throws Exception {
        assertEquals(INITIAL_COUNT, sut.getCount());
    }

    @Test
    public void itemCountOnUpdate() throws Exception {
        viewModelsSource.onNext(RecyclerViewAdapterTest.dummyViewModels(4));

        assertEquals(4, sut.getCount());
    }

    @Test
    public void notifyIsCalledOnUpdate() throws Exception {
        viewModelsSource.onNext(RecyclerViewAdapterTest.dummyViewModels(4));

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

        sut.unregisterDataSetObserver(defaultObserver);

        assertEquals(0, subscriptionCounter.subscriptions - subscriptionCounter.unsubscriptions);
    }

    @Test
    public void nullListIsTreatedAsEmpty() throws Exception {
        viewModelsSource.onNext(null);

        assertEquals(0, sut.getCount());
    }

    @Test
    public void errorIsHandled() throws Exception {
        viewModelsSource.onError(new Throwable());

        assertEquals(INITIAL_COUNT, sut.getCount());
    }

    @Test
    @UiThreadTest
    public void instantiateItemAddsBindedView() throws Exception {
        ViewGroup container = new LinearLayout(InstrumentationRegistry.getContext());
        sut.instantiateItem(container, 0);
        View child = container.getChildAt(0);

        ViewDataBinding binding = DataBindingUtil.bind(child);

        assertEquals(1, container.getChildCount());
        assertEquals("com.example.android_mvvm.test.databinding.LayoutTestBinding", binding.getClass().getName());
        assertTrue(testBinder.lastBinding == binding);
        assertTrue(testBinder.lastViewModel == viewModelsSource.getValue().get(0));
    }

    private class TestViewProvider implements ViewProvider {

        @Override
        public int getView(ViewModel vm) {
            return R.layout.layout_test;
        }
    }
}