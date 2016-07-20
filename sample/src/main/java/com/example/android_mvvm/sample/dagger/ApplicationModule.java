package com.example.android_mvvm.sample.dagger;

import android.content.Context;
import android.widget.Toast;

import com.example.android_mvvm.sample.adapters.ShowMessage;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    @Provides
    ShowMessage provideShowMessage(final Context context) {
        return new ShowMessage() {
            @Override
            public void show(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
