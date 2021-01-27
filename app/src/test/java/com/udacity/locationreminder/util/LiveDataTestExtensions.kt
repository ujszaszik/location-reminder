package com.udacity.locationreminder.util

import androidx.lifecycle.LiveData
import com.jraska.livedata.test

fun LiveData<Boolean>.testIfTrue() {
    test().assertHasValue().assertValue(true)
}

fun <T> LiveData<T>.testIfEquals(other: T) {
    test().assertHasValue().assertValue(other)
}

fun <T> LiveData<T>.testIfNull() {
    test().assertNullValue()
}