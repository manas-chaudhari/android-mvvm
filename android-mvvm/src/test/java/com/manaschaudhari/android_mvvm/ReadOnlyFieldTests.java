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

package com.manaschaudhari.android_mvvm;

import com.manaschaudhari.android_mvvm.testutils.SubscriptionCounter;

import org.junit.Before;
import org.junit.Test;


import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ReadOnlyFieldTests {
    private final Integer INITIAL_VALUE = 4;
    private BehaviorSubject<Integer> sourceSubject;
    private SubscriptionCounter<Integer> subscriptionCounter;
    private ReadOnlyField<Integer> sut;

    @Before
    public void setUp() throws Exception {
        sourceSubject = BehaviorSubject.createDefault(INITIAL_VALUE);
        subscriptionCounter = new SubscriptionCounter<>();
        Observable<Integer> source = sourceSubject.compose(subscriptionCounter);
        sut = ReadOnlyField.create(source);
    }

    @Test
    public void valueUpdates_OnAddingCallback() throws Exception {
        TestPropertyChangedCallback testCallback = new TestPropertyChangedCallback();
        sut.addOnPropertyChangedCallback(testCallback);

        assertThat(testCallback.callCount, is(1));
        assertThat(sut.get(), is(INITIAL_VALUE));

        sourceSubject.onNext(3);
        assertThat(testCallback.callCount, is(2));
        assertThat(sut.get(), is(3));
    }

    @Test
    public void noSubscription_initially() throws Exception {
        assertEquals(0, subscriptionCounter.subscriptions);
    }

    @Test
    public void noSubscription_afterRemovingCallback() throws Exception {
        TestPropertyChangedCallback callback = new TestPropertyChangedCallback();
        sut.addOnPropertyChangedCallback(callback);
        sut.removeOnPropertyChangedCallback(callback);

        assertEquals(0, subscriptionCounter.subscriptions - subscriptionCounter.unsubscriptions);
    }

    @Test
    public void singleSubscription_forMultipleCallbacks() throws Exception {
        TestPropertyChangedCallback callback = new TestPropertyChangedCallback();
        sut.addOnPropertyChangedCallback(callback);
        sut.addOnPropertyChangedCallback(callback);

        assertEquals(1, subscriptionCounter.subscriptions);
    }

    @Test
    public void errorIsHandled() throws Exception {
        sut.addOnPropertyChangedCallback(new TestPropertyChangedCallback());

        sourceSubject.onError(new Throwable());

        assertEquals(INITIAL_VALUE, sut.get());
    }

    public class TestPropertyChangedCallback extends android.databinding.Observable.OnPropertyChangedCallback {
        public int callCount;

        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            callCount++;
        }
    }
}