package com.udacity.locationreminder.database

import androidx.room.Room
import com.udacity.locationreminder.database.datasource.LocalDataSource
import com.udacity.locationreminder.database.datasource.ReminderLocalDataSource
import com.udacity.locationreminder.database.repository.ReminderRepository
import com.udacity.locationreminder.database.repository.Repository
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val databaseModule =
    module {

        // REMINDER DATABASE
        single {
            Room.databaseBuilder(
                androidContext(),
                ReminderDatabase::class.java,
                ReminderDatabase.DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }

        // REMINDER REPOSITORY
        single<Repository> {
            ReminderRepository(
                get(),
                Dispatchers.IO
            )
        }

        // REMINDER LOCAL DATA SOURCE
        single<LocalDataSource> {
            ReminderLocalDataSource(get())
        }
    }