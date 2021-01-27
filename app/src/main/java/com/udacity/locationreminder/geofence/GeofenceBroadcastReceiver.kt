package com.udacity.locationreminder.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.locationreminder.R
import com.udacity.locationreminder.geofence.message.GeofenceErrorMessageHandler
import com.udacity.locationreminder.notification.ReminderNotificator
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private lateinit var reminderNotificator: ReminderNotificator
    private lateinit var errorMessageHandler: GeofenceErrorMessageHandler

    override fun onReceive(context: Context?, intent: Intent?) {

        initializeNotificatorAndErrorHandler(context)

        if (isGeofenceBroadcast(intent)) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                errorMessageHandler.showErrorMessage(geofencingEvent.errorCode)
                return
            }

            if (geofencingEvent.isEnterTransition() && geofencingEvent.isAnyTriggering()) {
                val requestId = geofencingEvent.getCurrentRequestId()
                val reminderName =
                    intent.getStringExtraByResId(context, R.string.broadcast_reminder_name_extra)

                reminderNotificator.sendNotification(requestId, reminderName)
            }
        }
    }

    private fun initializeNotificatorAndErrorHandler(context: Context?) {
        reminderNotificator = ReminderNotificator(context!!)
        errorMessageHandler = GeofenceErrorMessageHandler(context)
    }

    private fun isGeofenceBroadcast(intent: Intent?): Boolean {
        return intent?.action == GeofenceManager.ACTION_GEOFENCE_EVENT
    }

    private fun GeofencingEvent.isEnterTransition(): Boolean {
        return geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
    }

    private fun GeofencingEvent.isAnyTriggering(): Boolean {
        return triggeringGeofences.isNotEmpty()
    }

    private fun GeofencingEvent.getCurrentRequestId(): String {
        return triggeringGeofences[0].requestId
    }
}

private fun Intent?.getStringExtraByResId(context: Context?, resourceId: Int): String {
    return this?.let {
        extras?.get(context?.getString(resourceId))
    } as String
}