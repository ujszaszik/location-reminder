package com.udacity.locationreminder.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.koin.dsl.module

val daoTestModule =
    module {

        // TEST DATABASE
        single {
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ReminderDatabase::class.java
            ).allowMainThreadQueries()
        }

    }