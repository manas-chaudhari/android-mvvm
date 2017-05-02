package com.manaschaudhari.android_mvvm

import android.support.annotation.CheckResult
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue

@CheckResult
@CheckReturnValue
fun <T, E> Observable<T>.flatMapFirst(obs: Single<E>): Observable<E> = this
        .toFlowable(io.reactivex.BackpressureStrategy.DROP)
        .flatMap({ obs.toFlowable() }, 1)
        .toObservable()

@CheckResult
@CheckReturnValue
fun <T, E> Observable<T>.flatMapFirst(obs: Observable<E>): Observable<E> = this
        .toFlowable(io.reactivex.BackpressureStrategy.DROP)
        .flatMap({ obs.toFlowable(BackpressureStrategy.DROP) }, 1)
        .toObservable()