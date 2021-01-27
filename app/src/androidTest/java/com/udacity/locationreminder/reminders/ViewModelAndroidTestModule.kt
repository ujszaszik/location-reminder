package com.udacity.locationreminder.reminders

import com.udacity.locationreminder.database.FakeLocalDataSource
import com.udacity.locationreminder.database.repository.ReminderRepository
import com.udacity.locationreminder.geofence.FakeGeofenceTestManager
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val viewModelAndroidTestModule = module {
    viewModel(override = true) {
        RemindersSharedViewModel(
            ReminderRepository(
                FakeLocalDataSource(),
                Dispatchers.Unconfined
            ), FakeGeofenceTestManager()
        )
    }
}