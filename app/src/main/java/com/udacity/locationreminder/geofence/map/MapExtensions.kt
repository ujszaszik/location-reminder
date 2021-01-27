package com.udacity.locationreminder.geofence.map

import android.graphics.Color
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.udacity.locationreminder.geofence.GeofenceData
import com.udacity.locationreminder.location.latLng

fun GoogleMap.addMarkerTo(poi: PointOfInterest): Marker {
    return addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
}

private const val SAVED_MARKER_HUE = 120.0f

fun GoogleMap.addMarkerToGeofence(data: GeofenceData, isEditable: Boolean): Marker {
    val markerOptions = MarkerOptions().position(data.getLatLng()).title(data.name)
    if (!isEditable) markerOptions.icon(BitmapDescriptorFactory.defaultMarker(SAVED_MARKER_HUE))
    return addMarker(markerOptions)
}

private const val DEFAULT_STROKE_WIDTH = 8f

fun GoogleMap.addCircleTo(latLng: LatLng, radius: Double): Circle {
    return CircleOptions()
        .center(latLng)
        .radius(radius)
        .strokeColor(Color.RED)
        .strokeWidth(DEFAULT_STROKE_WIDTH)
        .run { addCircle(this) }
}

private const val DEFAULT_ZOOM_LEVEL = 15.0f

fun GoogleMap.moveCameraTo(location: Location, zoomLevel: Float = DEFAULT_ZOOM_LEVEL) {
    moveCamera(CameraUpdateFactory.newLatLngZoom(location.latLng(), zoomLevel))
}

fun GoogleMap.moveCameraTo(data: GeofenceData, zoomLevel: Float = DEFAULT_ZOOM_LEVEL) {
    moveCamera(CameraUpdateFactory.newLatLngZoom(data.getLatLng(), zoomLevel))
}

fun GoogleMap.setTypeAndResetTheme(typeId: Int) {
    mapType = typeId
    setMapStyle(null)
}

fun GoogleMap.enableZoom() {
    uiSettings.isZoomControlsEnabled = true
    uiSettings.isZoomGesturesEnabled = true
}