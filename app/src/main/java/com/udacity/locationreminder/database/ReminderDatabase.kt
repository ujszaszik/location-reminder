package com.udacity.locationreminder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.database.typeconverter.CoordinateTypeConverter
import com.udacity.locationreminder.database.typeconverter.LocalDateTimeTypeConverter
import com.udacity.locationreminder.database.typeconverter.ReminderStatusTypeConverter

@Database(entities = [ReminderEntity::class], exportSchema = false, version = 4)
@TypeConverters(
    CoordinateTypeConverter::class,
    ReminderStatusTypeConverter::class,
    LocalDateTimeTypeConverter::class
)
abstract class ReminderDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "ReminderDatabase"
    }

    abstract fun reminderDao(): ReminderDao
}