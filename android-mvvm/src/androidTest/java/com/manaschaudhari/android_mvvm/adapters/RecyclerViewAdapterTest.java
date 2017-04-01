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

import android.databinding.ViewDataBinding;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.testutils.SubscriptionCounter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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

    @BeforeClass
    public static void setup() {
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @AfterClass
    public static void tearDown() {
        RxAndroidPlugins.reset();
    }

    @Before
    public void setUp() throws Exception {
        List<ViewModel> vms = TestViewModel.dummyViewModels(INITIAL_COUNT);

        viewModelsSource = BehaviorSubject.createDefault(vms);
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
        viewModelsSource.onNext(TestViewModel.dummyViewModels(10));

        assertEquals(10, sut.getItemCount());
    }

    @Test
    public void notifyIsCalledOnUpdate() throws Exception {
        viewModelsSource.onNext(TestViewModel.dummyViewModels(4));

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
    public void itemTypeIsBasedOnViewProvider() throws Exception {
        List<ViewModel> vms = TestViewModel.dummyViewModels(4);
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
        RecyclerViewAdapter.DataBindingViewHolder holder = sut.onCreateViewHolder(parent, com.manaschaudhari.android_mvvm.test.R.layout.layout_test);

        assertNotNull(holder);
        // Class isn't available at compile time
        assertEquals("com.manaschaudhari.android_mvvm.test.databinding.LayoutTestBinding", holder.viewBinding.getClass().getName());
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

    @Test
    @UiThreadTest
    public void recycleUnbindsViewModel() throws Exception {
        View view = new View(InstrumentationRegistry.getContext());
        TestViewDataBinding binding = new TestViewDataBinding(view);

        sut.onViewRecycled(new RecyclerViewAdapter.DataBindingViewHolder(binding));

        assertSame(binding, testBinder.lastBinding);
        assertNull(testBinder.lastViewModel);
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

}