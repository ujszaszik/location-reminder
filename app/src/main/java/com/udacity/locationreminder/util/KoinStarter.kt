package com.udacity.locationreminder.util

import android.app.Application
import com.udacity.locationreminder.database.databaseModule
import com.udacity.locationreminder.geofence.geofenceModule
import com.udacity.locationreminder.geofence.map.mapModule
import com.udacity.locationreminder.location.locationModule
import com.udacity.locationreminder.notification.notificationModule
import com.udacity.locationreminder.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object KoinStarter {

    fun start(app: Application) {
        startKoin {
            androidContext(app)
            modules(
                viewModelModule,
                locationModule,
                databaseModule,
                geofenceModule,
                notificationModule,
                mapModule
            )
        }
    }
}