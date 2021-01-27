package com.udacity.locationreminder.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.locationreminder.geofence.message.GeofenceMessageType

fun showToastMessage(context: Context, messageId: Int) {
    Toast.makeText(context, context.getString(messageId), Toast.LENGTH_SHORT)
        .also { ToastManager.listOfToasts.add(it) }
        .also { it.show() }
}


fun showGeofenceToast(context: Context, type: GeofenceMessageType) {
    showToastMessage(context, type.resourceId)
}

fun showSnackbarMessage(activity: Activity, messageId: Int) {
    Snackbar.make(
        activity.findViewById(android.R.id.content),
        activity.getString(messageId), Snackbar.LENGTH_SHORT
    ).show()
}

inline fun Fragment.navigate(navDirections: () -> NavDirections) {
    findNavController().navigate(navDirections.invoke())
}
