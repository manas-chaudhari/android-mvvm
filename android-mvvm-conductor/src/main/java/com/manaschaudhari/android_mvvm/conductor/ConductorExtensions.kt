package com.manaschaudhari.android_mvvm.conductor

import com.bluelinelabs.conductor.rxlifecycle2.ControllerEvent
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable

/**
 * Created by gabor on 2017. 05. 01..
 */

typealias ControllerLifecycleProvider = LifecycleProvider<ControllerEvent>

/**
 * Convenience function for [Observable]s to bind to a controller's lifecycle. This makes it possible
 * to terminate a stream when the specified [end] controller event occurs. Also, this function takes care
 * of resubscribing to the Observable when the specified [start] lifecycle event occurs.
 *
 * @param lp The lifecycle provider instance, usually the Controller itself (in case of an [RxController]).
 */
fun <E> Observable<E>.bindToLifecycle(lp: ControllerLifecycleProvider, start: ControllerEvent = ControllerEvent.ATTACH, end: ControllerEvent = ControllerEvent.DETACH): Observable<E> = lp.lifecycle()
        .filter { it == start }
        .bindUntilEvent(lp, ControllerEvent.DESTROY)
        .switchMap { this.bindUntilEvent(lp, end) }

/*
fun <E> Flowable<E>.bindToLifecycle(lp: ControllerLifecycleProvider, start: ControllerEvent = ControllerEvent.ATTACH, end: ControllerEvent = ControllerEvent.DETACH): Flowable<E> = lp.lifecycle()
        .filter { it == start }
        .bindUntilEvent(lp, ControllerEvent.DESTROY)
        .flo { this.bindUntilEvent(lp, end) }*/
