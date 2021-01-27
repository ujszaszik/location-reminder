package com.udacity.locationreminder.geofence

import android.app.Activity
import com.google.android.gms.location.GeofencingRequest
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val geofenceModule = module {

    // GEOFENCING REQUEST BUILDER
    single {
        GeofencingRequest.Builder().setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
    }

    //GEOFENCE MANAGER
    single<IGeofenceManager> { (activity: Activity) -> GeofenceManager(activity) }
}