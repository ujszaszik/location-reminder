package com.udacity.locationreminder

import android.app.Activity
import com.udacity.locationreminder.login.LoginViewModel
import com.udacity.locationreminder.reminders.RemindersSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val viewModelModule =
    module {

        // REMINDERS SHARED VIEW MODEL
        viewModel { (activity: Activity) ->
            RemindersSharedViewModel(get(), get { parametersOf(activity) })
        }

        // LOGIN VIEW MODEL
        viewModel { LoginViewModel() }
    }