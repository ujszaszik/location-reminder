package com.udacity.locationreminder.geofence.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.udacity.locationreminder.geofence.GeofenceData
import com.udacity.locationreminder.geofence.map.style.MapStyle
import com.udacity.locationreminder.reminders.ReminderType

@SuppressLint("MissingPermission")
class MapManager(private val map: GoogleMap) {

    internal lateinit var currentSelection: GeofenceData
    private lateinit var currentDrawnMarker: Marker
    private lateinit var currentDrawnCircle: Circle

    lateinit var markerDrawnCallback: MarkerCallback
    lateinit var markerRemovedCallback: MarkerCallback

    lateinit var type: ReminderType

    init {
        setPoiListener()
    }

    private fun setPoiListener() {
        map.setOnPoiClickListener {
            removePreviouslyDrawnMarker()
            currentDrawnMarker = map.addMarkerTo(it)
            currentSelection = GeofenceData.fromPoi(it)
            markerDrawnCallback.invoke()
        }
    }

    fun setCurrentMapType(context: Context, mapResourceId: Int, style: MapStyle) {
        if (style == MapStyle.MAP_TYPE) map.setTypeAndResetTheme(mapResourceId)
        else map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, mapResourceId))
    }

    fun jumpToCurrentLocation(location: Location) {
        map.moveCameraTo(location)
    }

    fun jumpToEditableLocation(data: GeofenceData) {
        currentDrawnMarker = map.addMarkerToGeofence(data, true)
        currentDrawnCircle = map.addCircleTo(data.getLatLng(), data.radius.toDouble())
        currentSelection = data
        map.moveCameraTo(data)
        markerDrawnCallback.invoke()
    }

    fun showAllGeofenceLocations(list: List<GeofenceData>) {
        list.forEach { map.addMarkerToGeofence(it, false) }
    }

    fun onRadiusChanged(newValue: Double) {
        if (this::currentSelection.isInitialized) {
            currentSelection.radius = newValue.toFloat()
            removePreviouslyDrawnCircle()
            currentDrawnCircle = map.addCircleTo(currentSelection.getLatLng(), newValue)
        }
    }

    private fun removePreviouslyDrawnMarker() {
        if (this::currentDrawnMarker.isInitialized) {
            currentDrawnMarker.remove()
            removePreviouslyDrawnCircle()
            markerRemovedCallback.invoke()
        }
    }

    private fun removePreviouslyDrawnCircle() {
        if (this::currentDrawnCircle.isInitialized) {
            currentDrawnCircle.remove()
        }
    }
}