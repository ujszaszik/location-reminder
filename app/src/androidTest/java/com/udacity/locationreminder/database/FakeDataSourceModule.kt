package com.udacity.locationreminder.database

import com.udacity.locationreminder.database.datasource.LocalDataSource
import com.udacity.locationreminder.database.repository.ReminderRepository
import com.udacity.locationreminder.database.repository.Repository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val fakeDataSourceModule =
    module {

        // REPOSITORY
        single<Repository>(override = true) {
            ReminderRepository(
                FakeLocalDataSource(),
                Dispatchers.Unconfined
            )
        }

        // FAKE DATA SOURCE
        single<LocalDataSource>(override = true) { FakeLocalDataSource() }
    }