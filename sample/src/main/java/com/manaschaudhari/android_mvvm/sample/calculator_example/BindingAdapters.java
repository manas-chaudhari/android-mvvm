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

import android.databinding.BindingConversion;
import android.databinding.InverseBindingAdapter;
import android.widget.RadioGroup;

import com.manaschaudhari.android_mvvm.sample.R;

@SuppressWarnings("unused")
public class BindingAdapters {

    /*
      Ideally, a BindingConversion from int -> Operation should be enough. However, as of
      gradle plugin 2.1.1, BindingConversion isn't supported for InverseBindingAdapters
     */
    @InverseBindingAdapter(attribute = "android:checkedButton")
    public static Calculator.Operation getOperation(RadioGroup radioGroup) {
        return toOperation(radioGroup.getCheckedRadioButtonId());
    }

    @BindingConversion
    public static int toLayout(Calculator.Operation operation) {
        if (operation == null) {
            return -1;
        }
        switch (operation) {
            case ADD:
                return R.id.radio_add;
            case SUBTRACT:
                return R.id.radio_subtract;
            case MULTIPLY:
                return R.id.radio_multiply;
            case DIVIDE:
                return R.id.radio_divide;
            default:
                return -1;
        }
    }

    @BindingConversion
    public static Calculator.Operation toOperation(int layoutId) {
        switch (layoutId) {
            case R.id.radio_add:
                return Calculator.Operation.ADD;
            case R.id.radio_subtract:
                return Calculator.Operation.SUBTRACT;
            case R.id.radio_multiply:
                return Calculator.Operation.MULTIPLY;
            case R.id.radio_divide:
                return Calculator.Operation.DIVIDE;
            default:
                return null;
        }
    }

}
