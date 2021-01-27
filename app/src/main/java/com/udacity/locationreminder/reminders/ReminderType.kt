package com.udacity.locationreminder.reminders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class ReminderType : Parcelable {
    CREATE, UPDATE
}