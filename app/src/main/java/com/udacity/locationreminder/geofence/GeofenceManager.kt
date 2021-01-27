package com.udacity.locationreminder.geofence

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.geofence.message.GeofenceMessageType
import com.udacity.locationreminder.util.showGeofenceToast
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@KoinApiExtension
open class GeofenceManager(private val activity: Activity) : KoinComponent, IGeofenceManager {

    companion object {
        const val ACTION_GEOFENCE_EVENT =
            "com.udacity.locationreminder.geofence.action.ACTION_GEOFENCE_EVENT"
    }

    private val geofencingClient: GeofencingClient by inject { parametersOf(activity) }

    private val requestBuilder: GeofencingRequest.Builder by inject()

    private fun createRequestWith(geofence: Geofence): GeofencingRequest {
        return requestBuilder
            .addGeofence(geofence)
            .build()
    }

    @SuppressLint("MissingPermission")
    override fun createGeofence(reminderEntity: ReminderEntity, isUpdate: Boolean) {
        val geofence = GeofenceFactory.fromEntity(reminderEntity)
        val pendingIntent = getPendingIntent(reminderEntity)
        geofencingClient.addGeofences(createRequestWith(geofence), pendingIntent)?.run {
            addOnSuccessListener {
                if (!isUpdate) showGeofenceToast(activity, GeofenceMessageType.SUCCESSFULLY_CREATED)
            }
            addOnFailureListener {
                if (!isUpdate) showGeofenceToast(activity, GeofenceMessageType.FAILED_AT_CREATION)
            }
        }
    }

    override fun removeGeofence(requestId: String, isUpdate: Boolean) {
        geofencingClient.removeGeofences(listOf(requestId))?.run {
            addOnSuccessListener {
                if (!isUpdate) showGeofenceToast(activity, GeofenceMessageType.SUCCESSFULLY_REMOVED)
            }
            addOnFailureListener {
                if (!isUpdate) showGeofenceToast(activity, GeofenceMessageType.FAILED_AT_REMOVAL)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun updateGeofence(reminderEntity: ReminderEntity) {
        try {
            removeGeofence(reminderEntity.requestId, true)
            createGeofence(reminderEntity, true)
            showGeofenceToast(activity, GeofenceMessageType.SUCCESSFULLY_UPDATED)
        } catch (e: Exception) {
            showGeofenceToast(activity, GeofenceMessageType.FAILED_AT_UPDATE)
        }
    }

    private fun getGeofenceIntent(reminderEntity: ReminderEntity): Intent {
        return Intent(activity, GeofenceBroadcastReceiver::class.java).apply {
            action = ACTION_GEOFENCE_EVENT
            putExtra(activity.getString(R.string.broadcast_reminder_name_extra), reminderEntity.locationName)
            putExtra(activity.getString(R.string.broadcast_reminder_request_id_extra), reminderEntity.requestId)
        }
    }

    private fun getPendingIntent(reminderEntity: ReminderEntity): PendingIntent {
        return PendingIntent.getBroadcast(
            activity,
            0,
            getGeofenceIntent(reminderEntity),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}