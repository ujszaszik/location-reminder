package com.udacity.locationreminder.reminders.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.reminders.ItemClickListener

class ReminderListAdapter(
    private val onEditCLickListener: ItemClickListener<ReminderEntity>,
    private val onDeleteClickListener: ItemClickListener<ReminderEntity>
) :
    ListAdapter<ReminderEntity, ReminderListViewHolder>(ReminderListItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderListViewHolder {
        return ReminderListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReminderListViewHolder, position: Int) {
        holder.bind(getItem(position), onEditCLickListener, onDeleteClickListener)
    }
}