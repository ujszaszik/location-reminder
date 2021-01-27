package com.udacity.locationreminder.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.latLng(): LatLng = LatLng(latitude, longitude)