package com.udacity.locationreminder.database.datasource

import androidx.lifecycle.LiveData
import com.udacity.locationreminder.database.ReminderDao
import com.udacity.locationreminder.database.ReminderDatabase
import com.udacity.locationreminder.database.entity.ReminderEntity

class ReminderLocalDataSource(reminderDatabase: ReminderDatabase):
    LocalDataSource {

    private val dao: ReminderDao = reminderDatabase.reminderDao()

    override fun getAllReminders(): LiveData<List<ReminderEntity>> {
        return dao.getAllReminders()
    }

    override suspend fun createReminder(reminderEntity: ReminderEntity) {
        dao.insertReminder(reminderEntity)
    }

    override suspend fun updateReminder(reminderEntity: ReminderEntity) {
        dao.updateReminder(reminderEntity)
    }

    override suspend fun deleteReminder(reminderEntity: ReminderEntity) {
        dao.deleteReminderById(reminderEntity.id.toInt())
    }

    override suspend fun deleteAllReminders() {
        dao.deleteAllReminders()
    }

    override suspend fun getReminderById(id: Int): LiveData<ReminderEntity> {
        return dao.getById(id)
    }
}