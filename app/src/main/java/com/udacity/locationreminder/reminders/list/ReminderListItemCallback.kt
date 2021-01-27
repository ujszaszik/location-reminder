package com.udacity.locationreminder.reminders.list

import androidx.recyclerview.widget.DiffUtil
import com.udacity.locationreminder.database.entity.ReminderEntity

object ReminderListItemCallback : DiffUtil.ItemCallback<ReminderEntity>() {

    override fun areItemsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
        return oldItem == newItem
    }
}