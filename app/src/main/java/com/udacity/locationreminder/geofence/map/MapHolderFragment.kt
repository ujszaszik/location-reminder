package com.udacity.locationreminder.geofence.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

abstract class MapHolderFragment : Fragment(), OnMapReadyCallback {

    protected lateinit var mapManager: MapManager
    protected lateinit var mapView: MapView

    protected fun createMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(activity)
        mapManager = get { parametersOf(googleMap) }
        onMapReadyCallback()
    }

    protected abstract fun onMapReadyCallback()

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}