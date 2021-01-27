package com.udacity.locationreminder.database.datasource

import androidx.lifecycle.LiveData
import com.udacity.locationreminder.database.entity.ReminderEntity

interface LocalDataSource {

    fun getAllReminders(): LiveData<List<ReminderEntity>>

    suspend fun createReminder(reminderEntity: ReminderEntity)

    suspend fun updateReminder(reminderEntity: ReminderEntity)

    suspend fun deleteReminder(reminderEntity: ReminderEntity)

    suspend fun deleteAllReminders()

    suspend fun getReminderById(id: Int): LiveData<ReminderEntity>
}
