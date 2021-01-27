package com.udacity.locationreminder.reminders.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.entity.Coordinate
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.databinding.FragmentManageRemindersBinding
import com.udacity.locationreminder.geofence.GeofenceData
import com.udacity.locationreminder.location.LocationSettingsUtil
import com.udacity.locationreminder.reminders.RemindersSharedViewModel
import com.udacity.locationreminder.util.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class ManageRemindersFragment : Fragment() {

    private lateinit var binding: FragmentManageRemindersBinding
    private val viewModel: RemindersSharedViewModel by viewModel { parametersOf(requireActivity()) }
    private val args by navArgs<ManageRemindersFragmentArgs>()

    private val locationSettingsUtil: LocationSettingsUtil by inject { parametersOf(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentManageRemindersBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        bindArguments()

        setBackButtonListener()

        observeDataSaved()
        observeIncompleteSave()
        observeMapRequest()
        observeIfLocationAccessGranted()

        setLocationIfSelected()

        return binding.root
    }

    private fun bindArguments() {
        args.run {
            binding.reminder = reminder ?: ReminderEntity.initial()
            binding.type = type
            bindLocationToReminder(binding.reminder, args.selectedLocation)
        }
    }

    private fun bindLocationToReminder(reminder: ReminderEntity?, data: GeofenceData?) {
        if (data != null && reminder != null) {
            with(reminder) {
                requestId = data.id
                coordinate = Coordinate.fromGeofenceData(data)
                radius = data.radius.toInt()
                locationName = data.name
            }
        }
    }

    private fun setBackButtonListener() {
        binding.backButton.setOnClickListener { navigateToRemindersList() }
    }

    private fun observeDataSaved() {
        viewModel.saveCompleted.observeIfTrue(this) {
            navigateToRemindersList()
            viewModel.resetSaveCompleted()
        }
    }

    private fun navigateToRemindersList() {
        navigate {
            ManageRemindersFragmentDirections
                .actionManageRemindersFragmentToReminderListFragment()
        }
    }

    private fun observeIncompleteSave() {
        wrapEspressoIdlingResource {
            viewModel.areFieldsEmpty.observeIfTrue(this) {
                showToastMessage(
                    requireContext(),
                    R.string.all_fields_are_mandatory
                )
                viewModel.resetEmptyFieldState()
            }
        }
    }

    private fun observeMapRequest() {
        viewModel.isMapRequested.observeIfTrue(this) {
            locationSettingsUtil.checkLocationSettings()
            viewModel.resetMapRequestStatus()
        }
    }

    private fun observeIfLocationAccessGranted() {
        locationSettingsUtil.isGranted.observeIfTrue(this) {
            navigateToLocationSelection()
            locationSettingsUtil.resetIfGranted()
        }
    }

    private fun navigateToLocationSelection() {
        ManageRemindersFragmentDirections
            .actionManageRemindersFragmentToLocationSelectorFragment(args.type, binding.reminder!!)
            .run { findNavController().navigate(this) }
    }

    private fun setLocationIfSelected() {
        args.selectedLocation?.let {
            with(binding.reminder!!) {
                coordinate = Coordinate.fromLatLng(it.getLatLng())
                locationName = it.name
                radius = it.radius.toInt()
            }
        }
    }
}