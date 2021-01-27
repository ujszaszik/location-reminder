package com.udacity.locationreminder.location

import android.app.Activity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val locationModule =
    module {

        //LOCATION PERMISSION UTIL
        single { (activity: Activity) -> LocationPermissionUtil(activity) }

        //LOCATION REQUEST UTIL
        single { (activity: Activity) -> LocationSettingsUtil(activity) }

        //LOCATION REQUEST
        single {
            LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_LOW_POWER
            }
        }

        //LOCATION SETTINGS REQUEST BUILDER
        single {
            LocationSettingsRequest.Builder().addLocationRequest(get())
        }

        //LOCATION SETTINGS CLIENT
        single { (activity: Activity) -> LocationServices.getSettingsClient(activity) }

        //FUSED LOCATION CLIENT
        single { (activity: Activity) -> LocationServices.getFusedLocationProviderClient(activity) }

        //GEOFENCING CLIENT
        single { (activity: Activity) -> LocationServices.getGeofencingClient(activity) }

    }
