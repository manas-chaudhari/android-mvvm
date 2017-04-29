package com.manaschaudhari.android_mvvm;

import android.content.Context;
import android.support.v4.content.Loader;

import java.lang.ref.WeakReference;

/**
 * Created by gabor on 2017. 03. 23..
 */

class ViewModelLoader<T extends ViewModel> extends Loader<T> {
    private final WeakReference<ViewModelFactory<T>> factory;
    private T viewModel;

    ViewModelLoader(Context context, ViewModelFactory<T> factory) {
        super(context);
        this.factory = new WeakReference<>(factory);
    }

    @Override
    protected void onStartLoading() {
        // If we already own an instance, simply deliver it.
        if (viewModel != null) {
            deliverResult(viewModel);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        // Create the ViewModel using the Factory
        viewModel = factory.get().createViewModel();

        // Deliver the result
        deliverResult(viewModel);
    }

    @Override
    protected void onReset() {
        if (viewModel != null) {
            viewModel.onDestroyed();
            viewModel = null;
        }
    }
}