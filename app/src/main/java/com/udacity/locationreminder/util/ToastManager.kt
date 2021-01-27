package com.udacity.locationreminder.util

import android.widget.Toast

object ToastManager {

    @Volatile
    var listOfToasts = mutableListOf<Toast>()

    @Synchronized
    fun cancelAllVisible() {
        wrapEspressoIdlingResource {
            listOfToasts.forEach { toast ->
                toast.view?.isShown?.let {
                    toast.cancel()
                }
            }
        }
    }
}