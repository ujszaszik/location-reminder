package com.udacity.locationreminder.database.repository

import androidx.lifecycle.LiveData
import com.udacity.locationreminder.database.datasource.LocalDataSource
import com.udacity.locationreminder.database.entity.ReminderEntity

interface Repository {

    val dataSource: LocalDataSource

    val remindersList: LiveData<List<ReminderEntity>>

    suspend fun createReminder(reminderEntity: ReminderEntity)

    suspend fun updateReminder(reminderEntity: ReminderEntity)

    suspend fun deleteReminder(reminderEntity: ReminderEntity)

}