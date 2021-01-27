package com.udacity.locationreminder.geofence

import com.udacity.locationreminder.database.entity.ReminderEntity

class FakeGeofenceTestManager() : IGeofenceManager {

    private val list = mutableListOf<GeofenceData>()

    override fun createGeofence(reminderEntity: ReminderEntity, isUpdate: Boolean) {
        list.add(GeofenceData.fromReminder(reminderEntity))
    }

    override fun removeGeofence(requestId: String, isUpdate: Boolean) {
        list.first { it.id == requestId }
    }

    override fun updateGeofence(reminderEntity: ReminderEntity) {
        removeGeofence(reminderEntity.requestId, false)
        createGeofence(reminderEntity, false)
    }
}