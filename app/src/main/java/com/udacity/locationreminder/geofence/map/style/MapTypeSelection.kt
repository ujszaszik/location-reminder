package com.udacity.locationreminder.geofence.map.style

import com.google.android.gms.maps.GoogleMap
import com.udacity.locationreminder.R


enum class MapTypeSelection(
    private val menuItemId: Int,
    private val mapType: Int
) {
    DEFAULT(R.id.defaultMapType, GoogleMap.MAP_TYPE_NORMAL),
    HYBRID(R.id.hybridMapType, GoogleMap.MAP_TYPE_HYBRID),
    SATELLITE(R.id.satelliteMapType, GoogleMap.MAP_TYPE_SATELLITE),
    TERRAIN(R.id.terrainMapType, GoogleMap.MAP_TYPE_TERRAIN);

    companion object {

        fun getSelectedMapType(selectedMenuItemId: Int): Int? {
            return values()
                .filter { selectedMenuItemId == it.menuItemId }
                .map { it.mapType }
                .firstOrNull()
        }
    }
}