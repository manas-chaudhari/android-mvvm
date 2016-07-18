package com.example.android_mvvm;

import android.databinding.ObservableField;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.example.android_mvvm.FieldUtils.toObservable;

public class FieldUtilsTest {

    public static final int INITIAL_VALUE = 4;
    private Observable<Integer> sut;
    private ObservableField<Integer> observableField;
    private TestSubscriber<Integer> testSubscriber;

    @Before
    public void setUp() throws Exception {
        observableField = new ObservableField<>(INITIAL_VALUE);
        sut = toObservable(observableField);
        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void emitsInitialValue() throws Exception {
        sut.subscribe(testSubscriber);

        testSubscriber.assertValues(INITIAL_VALUE);
    }

    @Test
    public void emitsUpdates() throws Exception {
        sut.subscribe(testSubscriber);
        observableField.set(3);

        testSubscriber.assertValues(INITIAL_VALUE, 3);
    }
}