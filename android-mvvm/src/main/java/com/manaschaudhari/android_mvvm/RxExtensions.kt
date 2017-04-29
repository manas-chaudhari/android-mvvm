package com.manaschaudhari.android_mvvm

import android.databinding.*
import android.support.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * A set of convenience extension functions for Kotlin.
 *
 * Example usage:
 * someObservableField.asObservable().....
 * Created by gabor on 2017. 03. 24..
 */

/**
 * From ObservableField to Observable
 */
fun <T> ObservableField<T>.asObservable(): Observable<T> {
    return FieldUtils.toObservable(this)
}

fun <T> ObservableInt.asObservable(): Observable<Int> {
    return FieldUtils.toObservable(this)
}

fun <T> ObservableFloat.asObservable(): Observable<Float> {
    return FieldUtils.toObservable(this)
}

fun <T> ObservableDouble.asObservable(): Observable<Double> {
    return FieldUtils.toObservable(this)
}

fun <T> ObservableLong.asObservable(): Observable<Long> {
    return FieldUtils.toObservable(this)
}

fun <T> ObservableList<T>.asObservable(): Observable<List<T>> {
    return FieldUtils.toObservable(this)
}

/**
 * From Observable to ObservableField
 */
fun <T> Observable<T>.asField(): ObservableField<T> {
    return FieldUtils.toField(this)
}

@CheckResult
@CheckReturnValue
fun <T> Observable<T>.bindTo(field: ObservableField<T>): Disposable {
    return FieldUtils.bindTo(this, field)
}

/**
 * Helper method for creating an Observable<Boolean> that emits true when the specified Observables emit something.
 * This is useful for creating a loading Observable, that emits true if a loader should be visible, and false when
 * it should be invisible. Finish the call with [endLoading].
 *
 * Example:
 * val loading = startLoading(button1Tap, button2Tap).endLoading(response1, response2)
 */
@CheckResult
@CheckReturnValue
fun startLoading(start1: Observable<*>, start2: Observable<Any> = PublishSubject.create()): Observable<Boolean> {
    return Observable.merge(start1.map { true }, start2.map { true })
}

@CheckResult
@CheckReturnValue
fun Observable<Boolean>.endLoading(end1: Observable<*>, end2: Observable<*>? = null): Observable<Boolean> {
    return Observable.merge(end1.map { false }, (end2 ?: Observable.never<Any>()).map { false })
/*    val _end2 = end2?.map { Any() } ?: Observable.just(Any())

    val endCombined: Observable<Boolean> = Observable.combineLatest(end1, _end2, BiFunction { _, _ -> false })
    return Observable.merge(this, endCombined)*/
}