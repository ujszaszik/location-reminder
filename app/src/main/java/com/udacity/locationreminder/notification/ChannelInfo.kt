package com.udacity.locationreminder.notification

import android.content.Context
import com.udacity.locationreminder.R

data class ChannelInfo constructor(
    val id: String,
    val name: String,
    val description: String
) {

    companion object {

        fun fromResources(context: Context): ChannelInfo {
            return ChannelInfo(
                context.getString(R.string.notification_channel_id),
                context.getString(R.string.notification_channel_name),
                context.getString(R.string.notification_channel_description)
            )
        }
    }
}