/*
 * Copyright 2016 Manas Chaudhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manaschaudhari.android_mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.test.R;
import com.manaschaudhari.android_mvvm.testutils.SubscriptionCounter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ViewPagerAdapterTest {

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    public static final int INITIAL_COUNT = 3;
    private TestViewPagerAdapter sut;
    private BehaviorSubject<List<ViewModel>> viewModelsSource;
    private SubscriptionCounter<List<ViewModel>> subscriptionCounter;
    private TestViewProvider testViewProvider;
    private TestViewModelBinder testBinder;
    private Disposable connection;

    @Before
    public void setUp() throws Exception {
        viewModelsSource = BehaviorSubject.createDefault(TestViewModel.dummyViewModels(INITIAL_COUNT));
        testViewProvider = new TestViewProvider();
        testBinder = new TestViewModelBinder();
        subscriptionCounter = new SubscriptionCounter<>();
        sut = new TestViewPagerAdapter(viewModelsSource.compose(subscriptionCounter),
                testViewProvider, testBinder);

        connection = sut.connect();
    }

    @Test
    public void initialItemCount() throws Exception {
        assertEquals(INITIAL_COUNT, sut.getCount());
    }

    @Test
    public void itemCountOnUpdate() throws Exception {
        viewModelsSource.onNext(TestViewModel.dummyViewModels(4));

        assertEquals(4, sut.getCount());
    }

    @Test
    public void notifyIsCalledOnUpdate() throws Exception {
        assertEquals(1, sut.notifyCount);

        viewModelsSource.onNext(TestViewModel.dummyViewModels(4));

        assertEquals(2, sut.notifyCount);
    }

    @Test
    public void noSubscriptionsInitially() throws Exception {
        SubscriptionCounter<List<ViewModel>> counter = new SubscriptionCounter<>();
        Observable<List<ViewModel>> source = viewModelsSource.compose(counter);

        ViewPagerAdapter sut = new ViewPagerAdapter(source, testViewProvider, testBinder);

        assertEquals(0, counter.subscriptions);
    }

    @Test
    public void noSubscriptionsAfterDisconnect() throws Exception {
        // defaultObserver is added in setup to measure notifyCount
        assertEquals(1, subscriptionCounter.subscriptions);
        assertEquals(0, subscriptionCounter.unsubscriptions);

        connection.dispose();

        assertEquals(0, subscriptionCounter.subscriptions - subscriptionCounter.unsubscriptions);
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
        assertEquals("com.manaschaudhari.android_mvvm.test.databinding.LayoutTestBinding", binding.getClass().getName());
        assertTrue(testBinder.lastBinding == binding);
        assertTrue(testBinder.lastViewModel == viewModelsSource.getValue().get(0));
    }

    @Test
    @UiThreadTest
    public void destroyItemUnbindsAndRemovesChildView() throws Exception {
        ViewGroup container = new LinearLayout(InstrumentationRegistry.getContext());
        Object pagerObject = sut.instantiateItem(container, 0);
        View child = container.getChildAt(0);
        ViewDataBinding binding = DataBindingUtil.bind(child);

        sut.destroyItem(container, 0, pagerObject);

        assertEquals(0, container.getChildCount());
        assertTrue(testBinder.lastBinding == binding);
        assertTrue(testBinder.lastViewModel == null);
    }

    @Test
    @UiThreadTest
    public void viewFromObject() throws Exception {
        ViewGroup container1 = new LinearLayout(InstrumentationRegistry.getContext());
        ViewGroup container2 = new LinearLayout(InstrumentationRegistry.getContext());
        Object pagerObject1 = sut.instantiateItem(container1, 0);
        Object pagerObject2 = sut.instantiateItem(container2, 1);
        View child1 = container1.getChildAt(0);
        View child2 = container2.getChildAt(0);

        assertTrue(sut.isViewFromObject(child1, pagerObject1));
        assertFalse(sut.isViewFromObject(child1, pagerObject2));
        assertTrue(sut.isViewFromObject(child2, pagerObject2));
        assertFalse(sut.isViewFromObject(child2, pagerObject1));
    }

    private static class TestViewProvider implements ViewProvider {

        @Override
        public int getView(ViewModel vm) {
            return R.layout.layout_test;
        }
    }

    private static class TestViewPagerAdapter extends ViewPagerAdapter {
        int notifyCount = 0;

        public TestViewPagerAdapter(Observable<List<ViewModel>> viewModels, ViewProvider viewProvider, ViewModelBinder binder) {
            super(viewModels, viewProvider, binder);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            notifyCount++;
        }
    }
}