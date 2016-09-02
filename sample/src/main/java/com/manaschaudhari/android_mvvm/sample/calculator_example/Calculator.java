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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Calculator {

    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    @Nullable
    public Integer run(int a, int b, @NonNull Operation operation) {
        switch (operation) {
            case ADD:
                return a + b;
            case SUBTRACT:
                return a - b;
            case MULTIPLY:
                return a * b;
            case DIVIDE:
                if (b != 0) {
                    return a / b;
                } else {
                    return null;
                }
        }
        return null;
    }
}
