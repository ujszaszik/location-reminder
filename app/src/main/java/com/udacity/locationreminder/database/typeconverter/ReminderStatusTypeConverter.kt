package com.udacity.locationreminder.database.typeconverter

import androidx.room.TypeConverter
import com.udacity.locationreminder.database.entity.ReminderStatus

class ReminderStatusTypeConverter {

    companion object {

        @JvmStatic
        @TypeConverter
        fun convertToString(status: ReminderStatus): String? = status.textual

        @JvmStatic
        @TypeConverter
        fun convertToStatus(value: String): ReminderStatus? {
            return ReminderStatus.values().firstOrNull { value == it.textual }
        }
    }
}