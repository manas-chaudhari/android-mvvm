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

package com.manaschaudhari.android_mvvm

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder
import com.manaschaudhari.android_mvvm.utils.BindingUtils
import com.manaschaudhari.android_mvvm.utils.Preconditions
import com.trello.navi.component.support.NaviFragment

import io.reactivex.disposables.CompositeDisposable

/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library
 */
abstract class MvvmFragment<T : ViewModel, Binding : ViewDataBinding> : NaviFragment(), ViewModelFactory<T> {
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<Binding>(inflater!!, layoutId, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // LoaderCallbacks as an object, so no hint regarding loader will be leak to the subclasses.
        loaderManager.initLoader(LOADER_ID, null, object : LoaderManager.LoaderCallbacks<T> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<T> {
                return ViewModelLoader(context, this@MvvmFragment)
            }

            override fun onLoadFinished(loader: Loader<T>, viewModel: T) {
                this@MvvmFragment.viewModel = viewModel
            }

            override fun onLoaderReset(loader: Loader<T>) {
                this@MvvmFragment::class.java.getDeclaredField("viewModel").set(this, null)
            }
        })
    }

    override fun onResume() {
        super.onResume()
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

    /**
     * Called when the [ViewModel] has been loaded to this Fragment.
     * Here you can do your logic that you would normally do in [.onActivityCreated].
     */
    open protected fun onViewModelLoaded() {
    }

    /**
     * Called when the [ViewModel] has been unloaded from Fragment. This method unsubcribes from the [io.reactivex.disposables.Disposables]
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

    companion object {
        private val LOADER_ID = 102
    }
}