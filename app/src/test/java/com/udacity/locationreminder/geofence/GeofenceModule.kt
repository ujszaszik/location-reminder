package com.udacity.locationreminder.geofence

import org.koin.dsl.module

val geofenceTestModule =
    module {
        single<IGeofenceManager> { FakeGeofenceManager() }
    }