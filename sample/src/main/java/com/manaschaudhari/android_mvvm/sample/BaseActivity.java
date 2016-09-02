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

package com.manaschaudhari.android_mvvm.sample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.manaschaudhari.android_mvvm.MvvmActivity;
import com.manaschaudhari.android_mvvm.sample.adapters.ItemListActivity;
import com.manaschaudhari.android_mvvm.sample.adapters.MessageHelper;
import com.manaschaudhari.android_mvvm.sample.calculator_example.CalculatorActivity;
import com.manaschaudhari.android_mvvm.sample.functional.DataLoadingActivity;
import com.manaschaudhari.android_mvvm.sample.two_way_binding.SearchActivity;

public abstract class BaseActivity extends MvvmActivity {

    @NonNull
    protected Navigator getNavigator() {
        return new Navigator() {
            @Override
            public void openDetailsPage(Item item) {
                navigate(ItemDetailsActivity.class);
            }

            @Override
            public void navigateToFunctionalDemo() {
                navigate(DataLoadingActivity.class);
            }

            @Override
            public void navigateToAdapterDemo() {
                navigate(ItemListActivity.class);
            }

            @Override
            public void navigateToTwoWayBindingDemo() {
                navigate(SearchActivity.class);
            }

            @Override
            public void navigateToCalculatorDemo() {
                navigate(CalculatorActivity.class);
            }

            private void navigate(Class<?> destination) {
                Intent intent = new Intent(BaseActivity.this, destination);
                startActivity(intent);
            }
        };
    }

    @NonNull
    protected MessageHelper getMessageHelper() {
        return new MessageHelper() {
            @Override
            public void show(String message) {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

}
