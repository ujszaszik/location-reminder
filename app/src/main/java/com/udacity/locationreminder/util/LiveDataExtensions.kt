package com.udacity.locationreminder.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeNotNull(fragment: Fragment, block: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer {
        it?.let { block.invoke(it) }
    })
}

fun <T> LiveData<T>.observeIfNull(fragment: Fragment, block: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer {
        if (it == null) block.invoke(it)
    })
}

fun LiveData<Boolean>.observeIfTrue(fragment: Fragment, block: (Boolean) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer { event ->
        event?.let { if (it) block.invoke(it) }
    })
}