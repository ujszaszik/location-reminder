package com.udacity.locationreminder.util

import androidx.lifecycle.LiveData
import com.jraska.livedata.test
import org.junit.Assert

fun <T> LiveData<T>.assertValueEquals(other: T) {
    this.test().value().let { Assert.assertEquals(it, other) }
}

fun <T> LiveData<T>.assertIfNull() {
    this.test().assertNullValue()
}