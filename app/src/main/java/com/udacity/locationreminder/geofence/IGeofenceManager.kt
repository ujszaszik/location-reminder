package com.udacity.locationreminder.geofence

import com.udacity.locationreminder.database.entity.ReminderEntity

interface IGeofenceManager {

    fun createGeofence(reminderEntity: ReminderEntity, isUpdate: Boolean)

    fun removeGeofence(requestId: String, isUpdate: Boolean)

    fun updateGeofence(reminderEntity: ReminderEntity)
}