package com.udacity.locationreminder

import android.app.Application
import android.os.Build
import com.jakewharton.threetenabp.AndroidThreeTen
import com.udacity.locationreminder.util.KoinStarter

class App : Application() {

    companion object {
        private val currentVersion = Build.VERSION.SDK_INT
        private const val qVersion = Build.VERSION_CODES.Q

        fun isRunningOnAtLeastQ(): Boolean = currentVersion >= qVersion
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        KoinStarter.start(this)
    }

}