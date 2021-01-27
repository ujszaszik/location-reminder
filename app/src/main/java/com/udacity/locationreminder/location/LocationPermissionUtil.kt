package com.udacity.locationreminder.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.udacity.locationreminder.App

class LocationPermissionUtil(private val activity: Activity) {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestPermission() {
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (App.isRunningOnAtLeastQ()) {
            permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
        }

        ActivityCompat.requestPermissions(
            activity,
            permissionsArray,
            LocationRequestCode.forCurrentApiLevel()
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun isGranted(): Boolean {
        var result: Boolean = isForegroundPermissionGranted()
        if (App.isRunningOnAtLeastQ()) result = result && isBackgroundPermissionGranted()
        return result
    }

    private fun isForegroundPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isBackgroundPermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
    }

}