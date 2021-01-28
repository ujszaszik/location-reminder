package com.udacity.locationreminder.notification

import android.app.NotificationManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val notificationModule =
    module {

        // NOTIFICATION MANAGER
        single {
            androidContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }