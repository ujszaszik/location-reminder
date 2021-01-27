package com.udacity.locationreminder.geofence.message

import android.content.Context
import com.google.android.gms.location.GeofenceStatusCodes
import com.udacity.locationreminder.R
import com.udacity.locationreminder.util.showToastMessage

class GeofenceErrorMessageHandler(private val context: Context) {

    fun showErrorMessage(errorCode: Int) {
        val errorMessageId = when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> R.string.geofence_error_not_available
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> R.string.geofence_error_too_many_geofences
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> R.string.geofence_error_too_many_intents
            else -> R.string.geofence_error_unknown_error
        }
        showToastMessage(context, errorMessageId)
    }
}