package com.udacity.locationreminder.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.locationreminder.database.datasource.LocalDataSource
import com.udacity.locationreminder.database.entity.ReminderEntity

class FakeDataSource :
    LocalDataSource {

    private val list = mutableListOf<ReminderEntity>()

    private val reminders = MutableLiveData<List<ReminderEntity>>()

    var throwError: Boolean = false

    override fun getAllReminders(): LiveData<List<ReminderEntity>> {
        return reminders
    }

    override suspend fun createReminder(reminderEntity: ReminderEntity) {
        if (throwError) throw Exception()
        list.add(reminderEntity)
        refresh()
    }

    override suspend fun updateReminder(reminderEntity: ReminderEntity) {
        if (throwError) throw Exception()
        list.forEach {
            if (it.id == reminderEntity.id) {
                list.remove(it)
                list.add(reminderEntity)
                refresh()
                return
            }
        }
    }

    override suspend fun deleteReminder(reminderEntity: ReminderEntity) {
        list.remove(reminderEntity)
        refresh()
    }

    override suspend fun deleteAllReminders() {
        list.clear()
        refresh()
    }

    override suspend fun getReminderById(id: Int): LiveData<ReminderEntity> {
        val reminder = list.firstOrNull { it.id == id.toLong() }
        return reminder?.let { MutableLiveData<ReminderEntity>(it) } ?: MutableLiveData()
    }

    private fun refresh() {
        reminders.postValue(list)
    }
}