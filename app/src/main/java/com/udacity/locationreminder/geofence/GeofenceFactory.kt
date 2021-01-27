package com.udacity.locationreminder.geofence

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.Geofence.NEVER_EXPIRE
import com.udacity.locationreminder.database.entity.ReminderEntity

class GeofenceFactory {

    companion object {
        fun fromEntity(reminder: ReminderEntity): Geofence {
            return Geofence.Builder()
                .setRequestId(reminder.requestId)
                .setCircularRegion(
                    reminder.coordinate.latitude,
                    reminder.coordinate.longitude,
                    reminder.radius.toFloat()
                )
                .setExpirationDuration(NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()
        }
    }
}