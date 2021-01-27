package com.udacity.locationreminder.geofence

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.locationreminder.database.entity.ReminderEntity
import kotlinx.android.parcel.Parcelize

private const val DEFAULT_CIRCLE_RADIUS = 100.0f

@Parcelize
data class GeofenceData(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var radius: Float = DEFAULT_CIRCLE_RADIUS
) : Parcelable {

    companion object {

        fun fromPoi(poi: PointOfInterest): GeofenceData {
            return GeofenceData(
                poi.placeId,
                poi.name,
                poi.latLng.latitude,
                poi.latLng.longitude
            )
        }

        fun fromReminder(reminderEntity: ReminderEntity): GeofenceData {
            return GeofenceData(
                reminderEntity.requestId,
                reminderEntity.locationName,
                reminderEntity.coordinate.latitude,
                reminderEntity.coordinate.longitude
            )
        }

    }

    fun getLatLng(): LatLng = LatLng(latitude, longitude)

}