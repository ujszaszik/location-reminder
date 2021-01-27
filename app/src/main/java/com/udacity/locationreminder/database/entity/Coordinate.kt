package com.udacity.locationreminder.database.entity

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.udacity.locationreminder.geofence.GeofenceData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinate(
    var latitude: Double,
    var longitude: Double
) : Parcelable {

    override fun toString(): String {
        return StringBuilder().apply {
            append("[")
            append(latitude)
            append(",")
            append(longitude)
            append("]")
        }.toString()
    }

    companion object {
        fun fromString(value: String): Coordinate {
            val split =
                value.replace("[", "")
                    .replace("]", "")
                    .split(",")
            return Coordinate(
                split[0].toDouble(),
                split[1].toDouble()
            )
        }

        fun fromLatLng(latLng: LatLng): Coordinate {
            return Coordinate(
                latLng.latitude,
                latLng.longitude
            )
        }

        fun fromGeofenceData(data: GeofenceData): Coordinate {
            return Coordinate(data.latitude, data.longitude)
        }
    }

    fun isIncomplete() = latitude == 0.0 || longitude == 0.0
}