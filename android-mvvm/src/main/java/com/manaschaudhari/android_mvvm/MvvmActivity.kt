/*
 * Copyright 2016 Manas Chaudhari, 2017 Gabor Szanto
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

package com.manaschaudhari.android_mvvm

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder
import com.manaschaudhari.android_mvvm.utils.BindingUtils
import com.manaschaudhari.android_mvvm.utils.Preconditions
import com.trello.navi.component.support.NaviAppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library. This class also takes care of persisting the
 * [ViewModel] instance across config / orientation changes.
 */
abstract class MvvmActivity<T : ViewModel, Binding : ViewDataBinding> : NaviAppCompatActivity(), LoaderManager.LoaderCallbacks<T>, ViewModelFactory<T> {
    protected lateinit var binding: Binding
        private set
    protected val disposable = CompositeDisposable()
    private var loadedBefore = false

    /**
     * Subclasses should return a stored ViewModel instance here. Important:
     * the [ViewModel] must NOT be instantiated here. Return a stored
     * value instead!!!

     * @return The already instantiated ViewModel instance, or null if not yet
     * * instantiated.
     */
    /**
     * Subclasses should set the stored [ViewModel] instance here.

     * @param viewModel The ViewModel instance to store in the subclass.
     */
    abstract var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId != 0) {
            binding = DataBindingUtil.setContentView<Binding>(this, layoutId)
        }

        supportLoaderManager.initLoader(LOADER_ID, null, this)
    }

    private val defaultBinder: ViewModelBinder
        get() {
            val defaultBinder = BindingUtils.getDefaultBinder()
            Preconditions.checkNotNull<ViewModelBinder>(defaultBinder, "Default Binder")
            return defaultBinder!!
        }

    /**
     * Subclasses should create their ViewModel here, and store it
     * in an instance variable.
     */
    abstract fun onCreateViewModel()

    override fun createViewModel(): T {
        onCreateViewModel()
        return viewModel
    }

    override fun onStart() {
        super.onStart()

        if (!loadedBefore) {
            loadedBefore = true
            defaultBinder.bind(binding, viewModel)
            onViewModelLoaded()
            viewModel.onViewAttached()
        }
    }

    override fun onDestroy() {
        viewModel.onViewDetached()
        onViewModelUnloaded()

        defaultBinder.bind(binding, null)
        binding.executePendingBindings()

        super.onDestroy()
    }

    /**
     * Called when the [ViewModel] has been loaded to this [android.app.Activity].
     * Here you can do your logic that you would normally do in [.onCreate].
     */
    open protected fun onViewModelLoaded() {
    }

    /**
     * Called when the [ViewModel] has been unloaded from this [android.app.Activity].
     * This method unsubcribes from the [io.reactivex.disposables.Disposables]
     * that has been added via `getDisposable() `.
     */
    @CallSuper
    open protected fun onViewModelUnloaded() {
        disposable.clear()
    }

    /**
     * Subclasses should return the ID of the layout which they
     * would like to inflate.

     * @return The ID of the layout to be inflated.
     */
    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<T> {
        return ViewModelLoader(this, this)
    }

    override fun onLoadFinished(loader: Loader<T>, data: T) {
        this.viewModel = data
    }

    override fun onLoaderReset(loader: Loader<T>) {
        this::class.java.getDeclaredField("viewModel").set(this, null)
    }

    companion object {
        private val LOADER_ID = 101
    }
}