package com.example.android_mvvm.sample;

import android.app.Application;
import android.content.Context;
import android.databinding.ViewDataBinding;

import com.example.android_mvvm.sample.dagger.ApplicationComponent;
import com.example.android_mvvm.sample.dagger.ApplicationModule;
import com.example.android_mvvm.sample.dagger.DaggerApplicationComponent;
import com.example.android_mvvm.ViewModel;
import com.example.android_mvvm.adapters.ViewModelBinder;
import com.example.android_mvvm.utils.BindingUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class ExampleApplication extends Application {
    public static RefWatcher getRefWatcher(Context context) {
        ExampleApplication application = (ExampleApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        BindingUtils.setDefaultBinder(new ViewModelBinder() {
            @Override
            public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
                viewDataBinding.setVariable(com.example.android_mvvm.sample.BR.vm, viewModel);
            }
        });
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((ExampleApplication)context.getApplicationContext()).applicationComponent;
    }
}
