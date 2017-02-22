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

import android.databinding.ObservableField;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static com.manaschaudhari.android_mvvm.FieldUtils.toObservable;

public class FieldUtilsTest {

    public static final int INITIAL_VALUE = 4;
    private Observable<Integer> sut;
    private ObservableField<Integer> observableField;
    private TestObserver<Integer> testObserver;

    @Before
    public void setUp() throws Exception {
        observableField = new ObservableField<>(INITIAL_VALUE);
        sut = toObservable(observableField);
        testObserver = new TestObserver<>();
    }

    @Test
    public void emitsInitialValue() throws Exception {
        sut.subscribe(testObserver);

        testObserver.assertValues(INITIAL_VALUE);
    }

    @Test
    public void emitsUpdates() throws Exception {
        sut.subscribe(testObserver);
        observableField.set(3);

        testObserver.assertValues(INITIAL_VALUE, 3);
    }
}