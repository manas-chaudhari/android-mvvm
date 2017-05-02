package com.manaschaudhari.android_mvvm.conductor

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.rxlifecycle2.RxController
import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder
import com.manaschaudhari.android_mvvm.utils.BindingUtils
import com.trello.rxlifecycle2.internal.Preconditions

/**
 * A [Controller] that uses DataBinding to inflate a layout, and asks implementations
 * to create a [ViewModel] instance when necessary. This class is designed to integrate
 * with Conductor, and to take care of retaining the ViewModel across orientation changes.
 *
 * With that, it is possible for ViewModels to hold a reference to the [Controller] (either
 * directly, or indirectly), and not cause any memory leaks. This is possible
 * because the Controller will be retained across orientation changes as well.
 *
 * Created by Gabor Szanto on 2017. 04. 29..
 */
abstract class MvvmController<Binding : ViewDataBinding, ViewModel : com.manaschaudhari.android_mvvm.ViewModel> : RxController {
    constructor() : super()
    constructor(args: Bundle?) : super(args)

    /**
     * The [ViewModel] instance that will be held by implementations. This instance will be
     * constructed once, and persisted across orientation changes.
     */
    abstract var viewModel: ViewModel

    /**
     * The generic [Binding] instance. This will be recreated whenever the Controller decides
     * to recreate its View.
     */
    lateinit var binding: Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val vm = this::class.java.getDeclaredField("viewModel")
        vm.isAccessible = true
        if (vm.get(this) == null)
            createViewModel()

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onDestroyView(view: View) {
        val binding = this::class.java.getField("binding")
        binding.isAccessible = true
        binding.set(this, null)
        super.onDestroyView(view)
    }

    private val defaultBinder: com.manaschaudhari.android_mvvm.adapters.ViewModelBinder
        get() {
            val defaultBinder = BindingUtils.getDefaultBinder()
            Preconditions.checkNotNull<ViewModelBinder>(defaultBinder, "Default Binder")
            return defaultBinder!!
        }

    override fun onAttach(view: View) {
        super.onAttach(view)
        defaultBinder.bind(binding, viewModel)
    }

    override fun onDetach(view: View) {
        defaultBinder.bind(binding, null)
        binding.executePendingBindings()
        super.onDetach(view)
    }

    /**
     * The [Controller] instance is about to be destroyed, so we need to clear out the
     * reference to the [ViewModel].
     *
     * Note: we need to use reflection here to keep Kotlin happy about the nullability. :)
     */
    override fun onDestroy() {
        val viewModel = this::class.java.getField("viewModel")
        viewModel.isAccessible = true
        viewModel.set(this, null)
        super.onDestroy()
    }

    /**
     * Implementations should return the ID of the layout to be inflated.
     */
    abstract fun getLayoutId(): Int

    /**
     * Implementations should create the ViewModel in this method, either manually, or
     * by using dependency injection (e.g. Dagger 2)
     */
    abstract fun createViewModel()
}