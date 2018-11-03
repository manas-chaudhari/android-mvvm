package com.manaschaudhari.android_mvvm;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;
import com.manaschaudhari.android_mvvm.utils.Preconditions;

/**
 * Created by mike on 3/3/17.
 */

public class MvvmBinder<Binding extends ViewDataBinding> {
    protected Binding binding;
    
    public MvvmBinder() {
    }
    
    /**
     * Call after super.onCreate of activity
     *
     * @param activity to bind with
     * @return the ViewDataBinding that was created
     */
    public Binding onCreate(Activity activity) {
        IVMBinder binder = (IVMBinder) activity;
        binding = DataBindingUtil.setContentView(activity, binder.getLayoutId());
        getDefaultBinder().bind(binding, binder.createViewModel());
        return binding;
    }
    
    /**
     * Used with a Fragment
     *
     * @param context to bind with
     * @return the ViewDataBinding that was created
     */
    public Binding onCreate(Context context) {
        IVMBinder binder = (IVMBinder) context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), binder.getLayoutId(), null, false);
        getDefaultBinder().bind(binding, binder.createViewModel());
        return binding;
    }
    
    /**
     * call before super.onDestroy or super.onDestroyView
     */
    public void onDestroy() {
        getDefaultBinder().bind(binding, null);
        binding.executePendingBindings();
    }
    
    @NonNull
    private ViewModelBinder getDefaultBinder() {
        ViewModelBinder defaultBinder = BindingUtils.getDefaultBinder();
        Preconditions.checkNotNull(defaultBinder, "Default Binder");
        return defaultBinder;
    }
}
