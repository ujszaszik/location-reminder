package com.udacity.locationreminder.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.locationreminder.database.entity.ReminderEntity

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE status != \"Deleted\" ORDER BY last_modified DESC")
    fun getAllReminders(): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    fun getById(id: Int): LiveData<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReminder(reminderEntity: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Int)

    @Query("DELETE FROM reminders")
    fun deleteAllReminders()
}