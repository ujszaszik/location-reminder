package com.udacity.locationreminder.location.selector

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.udacity.locationreminder.R
import com.udacity.locationreminder.databinding.FragmentLocationSelectorBinding
import com.udacity.locationreminder.geofence.GeofenceData
import com.udacity.locationreminder.geofence.map.MapHolderFragment
import com.udacity.locationreminder.geofence.map.MarkerCallback
import com.udacity.locationreminder.geofence.map.style.MapStyleResolver
import com.udacity.locationreminder.reminders.ReminderType
import com.udacity.locationreminder.reminders.RemindersSharedViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class LocationSelectorFragment : MapHolderFragment() {

    private lateinit var binding: FragmentLocationSelectorBinding
    private val args by navArgs<LocationSelectorFragmentArgs>()
    private val viewModel: RemindersSharedViewModel by viewModel { parametersOf(requireActivity()) }

    private val fusedLocationClient: FusedLocationProviderClient by inject {
        parametersOf(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationSelectorBinding.inflate(layoutInflater)
        mapView = binding.mapView

        setHasOptionsMenu(true)

        createMap(savedInstanceState)

        setSeekBarChangedListener()
        setOnCancelClickedListener()
        setOnSaveClickedListener()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_type_menu, menu)
        MenuCompat.setGroupDividerEnabled(menu, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MapStyleResolver.getValue(item.itemId)?.let {
            mapManager.setCurrentMapType(requireContext(), it.id, it.type)
            true
        } ?: super.onOptionsItemSelected(item)
    }

    private fun setSeekBarChangedListener() {
        binding.locationSelector.setSeekBarChangedListener {
            mapManager.onRadiusChanged(it.toDouble())
        }
    }

    private fun setOnCancelClickedListener() {
        binding.locationSelector.setOnCancelClicked { navigateToReminderManagement(null) }
    }

    private fun setOnSaveClickedListener() {
        binding.locationSelector.setOnSaveClicked {
            navigateToReminderManagement(mapManager.currentSelection)
        }
    }

    private fun navigateToReminderManagement(data: GeofenceData?) {
        LocationSelectorFragmentDirections.actionLocationSelectorFragmentToManageRemindersFragment(
            args.reminder, args.type, data
        ).run { findNavController().navigate(this) }
    }

    override fun onMapReadyCallback() {
        initializeMarkerCallbacks()
        showSavedLocations()
        showStartingLocation()
    }

    private fun showSavedLocations() {
        val remindersList = viewModel.reminders.value
        remindersList?.let { list ->
            val geofenceList = list.map { GeofenceData.fromReminder(it) }
            mapManager.showAllGeofenceLocations(geofenceList)
        }
    }

    private fun showStartingLocation() {
        val isEditMode = args.type == ReminderType.UPDATE
        if (isEditMode) mapManager.jumpToEditableLocation(GeofenceData.fromReminder(args.reminder))
        else showCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { mapManager.jumpToCurrentLocation(it) }
            }
    }

    private fun initializeMarkerCallbacks() {
        mapManager.markerDrawnCallback = MarkerCallback { binding.locationSelector.onEnabled() }
        mapManager.markerRemovedCallback =
            MarkerCallback { binding.locationSelector.resetSeekBar() }
    }
}
