package com.udacity.locationreminder.location

import android.app.Activity
import android.content.IntentSender
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.udacity.locationreminder.R
import com.udacity.locationreminder.util.showToastMessage
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class LocationSettingsUtil(private val activity: Activity) : KoinComponent {

    val requestCode = 29

    private val settingsClient: SettingsClient by inject { parametersOf(activity) }
    private val requestBuilder: LocationSettingsRequest.Builder by inject()

    private val _isGranted = MutableLiveData<Boolean>()
    val isGranted: LiveData<Boolean> get() = _isGranted

    fun checkLocationSettings(resolve: Boolean = true) {
        settingsClient.checkLocationSettings(requestBuilder.build()).apply {
            addOnFailureListener { exception -> doOnFailure(resolve, exception) }
            addOnCompleteListener { if (it.isSuccessful) doOnSuccess() }
        }
    }

    private fun doOnFailure(resolve: Boolean, exception: Exception) {
        if (exception is ResolvableApiException && resolve) {
            try {
                startTurnOnLocationScreen(exception)
            } catch (sendEx: IntentSender.SendIntentException) {
                sendEx.printStackTrace()
            }
        } else {
            showToastMessage(activity, R.string.geofence_location_must_be_enabled)
        }
    }

    private fun startTurnOnLocationScreen(exception: ResolvableApiException) {
        exception.startResolutionForResult(activity, requestCode)
    }

    private fun doOnSuccess() {
        _isGranted.postValue(true)
    }

    fun resetIfGranted() {
        _isGranted.value = null
    }

}