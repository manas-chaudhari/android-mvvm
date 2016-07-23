package com.example.android_mvvm.sample.dagger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.android_mvvm.sample.Item;
import com.example.android_mvvm.sample.ItemDetailsActivity;
import com.example.android_mvvm.sample.Navigator;
import com.example.android_mvvm.sample.adapters.MessageHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class ActivityModule {
    public final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Context provideContext() {
        return activity;
    }

    @Provides
    MessageHelper provideMessageHelper(final Context context) {
        return new MessageHelper() {
            @Override
            public void show(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Provides
    Navigator provideNavigator(final Context context) {
        return new Navigator() {
            @Override
            public void openDetailsPage(Item item) {
                context.startActivity(new Intent(context, ItemDetailsActivity.class));
            }
        };
    }
}
