package com.udacity.locationreminder.geofence.map

class MarkerCallback(private val callback: () -> Unit) {

    fun invoke() = callback.invoke()
}