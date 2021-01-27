package com.udacity.locationreminder.geofence.message

import com.udacity.locationreminder.R

enum class GeofenceMessageType(internal val resourceId: Int) {
    SUCCESSFULLY_CREATED(R.string.geofence_create_successful),
    FAILED_AT_CREATION(R.string.geofence_create_failed),
    SUCCESSFULLY_REMOVED(R.string.geofence_remove_successful),
    FAILED_AT_REMOVAL(R.string.geofence_remove_failed),
    SUCCESSFULLY_UPDATED(R.string.geofence_update_successful),
    FAILED_AT_UPDATE(R.string.geofence_update_failed)
}