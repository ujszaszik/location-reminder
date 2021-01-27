package com.udacity.locationreminder.database.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class ReminderStatus(val textual: String) : Parcelable {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETED("Deleted")
}