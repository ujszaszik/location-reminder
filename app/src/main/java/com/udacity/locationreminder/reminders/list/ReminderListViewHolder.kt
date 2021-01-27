package com.udacity.locationreminder.reminders.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.databinding.ReminderListRecyclerRowBinding
import com.udacity.locationreminder.reminders.ItemClickListener

class ReminderListViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        private lateinit var binding: ReminderListRecyclerRowBinding

        fun from(parent: ViewGroup): ReminderListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            binding = ReminderListRecyclerRowBinding.inflate(inflater, parent, false)
            return ReminderListViewHolder(binding.root)
        }
    }

    fun bind(
        reminder: ReminderEntity,
        onEditClickListener: ItemClickListener<ReminderEntity>,
        onDeleteClickListener: ItemClickListener<ReminderEntity>
    ) {
        binding.reminder = reminder
        binding.onEditClickListener = onEditClickListener
        binding.onDeleteClickListener = onDeleteClickListener
        binding.executePendingBindings()
    }
}