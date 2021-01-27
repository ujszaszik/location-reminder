package com.udacity.locationreminder.database

import com.udacity.locationreminder.database.datasource.LocalDataSource
import com.udacity.locationreminder.database.repository.ReminderRepository
import com.udacity.locationreminder.database.repository.Repository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataSourceModule =
    module {

        // FAKE REPOSITORY
        single<Repository> {
            ReminderRepository(
                get(),
                Dispatchers.Unconfined
            )
        }

        // FAKE DATA SOURCE
        single<LocalDataSource> { FakeDataSource() }
    }