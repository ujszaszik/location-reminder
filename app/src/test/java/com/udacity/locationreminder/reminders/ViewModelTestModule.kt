package com.udacity.locationreminder.reminders

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val viewModelTestModule =
    module {
        viewModel { RemindersSharedViewModel(get(), get()) }
    }