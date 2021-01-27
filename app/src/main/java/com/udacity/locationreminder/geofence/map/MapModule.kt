package com.udacity.locationreminder.geofence.map

import com.google.android.gms.maps.GoogleMap
import org.koin.dsl.module

val mapModule =
    module {
        //MAP MANAGER
        factory { (map: GoogleMap) -> MapManager(map) }
    }