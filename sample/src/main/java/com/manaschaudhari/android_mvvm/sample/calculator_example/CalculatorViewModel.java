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

package com.manaschaudhari.android_mvvm.sample.calculator_example;

import android.databinding.ObservableField;

import com.manaschaudhari.android_mvvm.ReadOnlyField;
import com.manaschaudhari.android_mvvm.ViewModel;

import io.reactivex.Observable;
import io.reactivex.functions.Function3;

import static com.manaschaudhari.android_mvvm.FieldUtils.toField;
import static com.manaschaudhari.android_mvvm.FieldUtils.toObservable;

public class CalculatorViewModel implements ViewModel {

    public final ObservableField<String> number1 = new ObservableField<>("");
    public final ObservableField<String> number2 = new ObservableField<>("");
    public final ObservableField<Calculator.Operation> operation = new ObservableField<>(Calculator.Operation.ADD);

    public final ReadOnlyField<String> result;

    public CalculatorViewModel() {
        final Calculator calculator = new Calculator();

        Observable<String> result = Observable.combineLatest(
                toObservable(number1), toObservable(number2), toObservable(operation),
                new Function3<String, String, Calculator.Operation, String>() {
                    @Override
                    public String apply(String s1, String s2, Calculator.Operation operation) throws Exception {
                        try {
                            int n1 = Integer.parseInt(s1);
                            int n2 = Integer.parseInt(s2);

                            Integer result = calculator.run(n1, n2, operation);
                            return (result != null) ? result.toString() : "ERROR";

                        } catch (NumberFormatException e) {
                            return "";
                        }
                    }
                });
        this.result = toField(result);
    }
}
