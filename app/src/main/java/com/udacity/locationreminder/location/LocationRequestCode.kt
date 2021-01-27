package com.udacity.locationreminder.location

import com.udacity.locationreminder.App

object LocationRequestCode {

    private const val resultCodeForBackgroundAndForeground = 33
    private const val resultCodeForOnlyForeground = 34

    fun forCurrentApiLevel(): Int {
        return when {
            App.isRunningOnAtLeastQ() -> resultCodeForBackgroundAndForeground
            else -> resultCodeForOnlyForeground
        }
    }
}