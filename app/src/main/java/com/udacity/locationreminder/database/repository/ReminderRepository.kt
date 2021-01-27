package com.udacity.locationreminder.database.repository

import androidx.lifecycle.LiveData
import com.udacity.locationreminder.database.datasource.LocalDataSource
import com.udacity.locationreminder.database.entity.ReminderEntity
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ReminderRepository(
    override val dataSource: LocalDataSource,
    private val coroutineContext: CoroutineContext
) : Repository {

    override val remindersList: LiveData<List<ReminderEntity>> = dataSource.getAllReminders()

    override suspend fun createReminder(reminderEntity: ReminderEntity) {
        withContext(coroutineContext) { dataSource.createReminder(reminderEntity) }
    }

    override suspend fun updateReminder(reminderEntity: ReminderEntity) {
        withContext(coroutineContext) { dataSource.updateReminder(reminderEntity) }
    }

    override suspend fun deleteReminder(reminderEntity: ReminderEntity) {
        withContext(coroutineContext) { dataSource.deleteReminder(reminderEntity) }
    }

}