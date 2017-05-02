package com.manaschaudhari.android_mvvm.conductor

import com.bluelinelabs.conductor.rxlifecycle2.ControllerEvent
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
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
fun <E> Observable<E>.attachToLifecycle(lp: ControllerLifecycleProvider, start: ControllerEvent = ControllerEvent.ATTACH, end: ControllerEvent = ControllerEvent.DETACH): Observable<E> = lp.lifecycle()
        .filter { it == start }
        .switchMap { this.bindUntilEvent(lp, end) }
        .bindUntilEvent(lp, ControllerEvent.DESTROY)

/*fun <E> Single<E>.attachToLifecycle(lp: ControllerLifecycleProvider, start: ControllerEvent = ControllerEvent.ATTACH, end: ControllerEvent = ControllerEvent.DETACH): Single<E> = lp.lifecycle()
        .filter { it == start }
        .flatMapSingle { this.bindUntilEvent(lp, end) }
        .bindUntilEvent(lp, ControllerEvent.DESTROY)*/

fun <E> Flowable<E>.attachToLifecycle(lp: ControllerLifecycleProvider, start: ControllerEvent = ControllerEvent.ATTACH, end: ControllerEvent = ControllerEvent.DETACH): Flowable<E> = lp.lifecycle()
        .toFlowable(BackpressureStrategy.LATEST)
        .filter { it == start }
        .switchMap { this.bindUntilEvent(lp, end) }
        .bindUntilEvent(lp, ControllerEvent.DESTROY)

fun Completable.attachToLifecycle(lp: ControllerLifecycleProvider, start: ControllerEvent = ControllerEvent.ATTACH, end: ControllerEvent = ControllerEvent.DETACH): Completable = lp.lifecycle()
        .filter { it == start }
        .flatMapCompletable { this.bindUntilEvent(lp, end) }
        .bindUntilEvent(lp, ControllerEvent.DESTROY)
