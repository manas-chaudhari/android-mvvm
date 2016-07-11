package com.example.android_mvvm;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.BehaviorSubject;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ReadOnlyFieldTests {
    private final Integer INITIAL_VALUE = 4;
    private BehaviorSubject<Integer> sourceSubject;
    private int subscriptions = 0;
    private int unsubscriptions = 0;
    private ReadOnlyField<Integer> sut;

    @Before
    public void setUp() throws Exception {
        sourceSubject = BehaviorSubject.create(INITIAL_VALUE);
        Observable<Integer> source = sourceSubject.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                subscriptions++;
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                unsubscriptions++;
            }
        });
        sut = new ReadOnlyField<>(source);
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
        assertEquals(0, subscriptions);
    }

    @Test
    public void noSubscription_afterRemovingCallback() throws Exception {
        TestPropertyChangedCallback callback = new TestPropertyChangedCallback();
        sut.addOnPropertyChangedCallback(callback);
        sut.removeOnPropertyChangedCallback(callback);

        assertEquals(0, subscriptions - unsubscriptions);
    }

    @Test
    public void singleSubscription_forMultipleCallbacks() throws Exception {
        TestPropertyChangedCallback callback = new TestPropertyChangedCallback();
        sut.addOnPropertyChangedCallback(callback);
        sut.addOnPropertyChangedCallback(callback);

        assertEquals(1, subscriptions);
    }

    public class TestPropertyChangedCallback extends android.databinding.Observable.OnPropertyChangedCallback {
        public int callCount;

        @Override
        public void onPropertyChanged(android.databinding.Observable observable, int i) {
            callCount++;
        }
    }
}