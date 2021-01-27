package com.udacity.locationreminder.reminders.detail

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.typeconverter.dateFormatter
import org.threeten.bp.LocalDateTime

@BindingAdapter("lastModified")
fun TextView.lastModified(date: LocalDateTime?) {
    date?.let {
        text = dateFormatter.format(it)
    }
}

@BindingAdapter("locationRadius")
fun TextView.locationRadius(radius: Int) {
    text = context.getString(R.string.radius_in_meters, radius)
}