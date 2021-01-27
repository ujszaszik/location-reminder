package com.udacity.locationreminder.util

import com.udacity.locationreminder.database.entity.Coordinate
import com.udacity.locationreminder.database.entity.ReminderEntity

object TestReminders {

    const val MODIFIED_LOCATION_NAME = "modifiedLocationName"
    const val MODIFIED_DESCRIPTION = "modifiedDescription"
    val MODIFIED_COORDINATE = Coordinate(100.0, 100.0)
    const val MODIFIED_RADIUS = 100

    val first = ReminderEntity.forTesting()

    val second = ReminderEntity(
        2, "test2", "title2", "description2", "location2",
        Coordinate(50.0, 50.0), 30
    )

    val third = ReminderEntity(
        3, "test3", "title3", "description3", "location3",
        Coordinate(75.0, 75.0), 45
    )

    val modifiedTestReminder = ReminderEntity(
        first.id,
        first.requestId,
        first.title,
        MODIFIED_DESCRIPTION,
        MODIFIED_LOCATION_NAME,
        MODIFIED_COORDINATE,
        MODIFIED_RADIUS
    )

    fun getAllTestReminders() = listOf(first, second, third)
}