package com.udacity.locationreminder.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.udacity.locationreminder.R
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class ReminderNotificator(private val context: Context) : KoinComponent {

    private val channelInfo = ChannelInfo.fromResources(context)

    fun sendNotification(reminderId: String, locationName: String) {
        val builder = getNotificationBuilder(reminderId, locationName)
        val notificationManager: NotificationManager by inject()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager, builder)
        }
        val notificationId = context.getString(R.string.notification_id).toInt()
        notificationManager.notify(notificationId, builder.build())
    }

    private fun getNotificationBuilder(
        reminderId: String,
        locationName: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelInfo.id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_content_title))
            .setContentText(context.getString(R.string.notification_content_message, locationName))
            .setAutoCancel(true)
            .apply { addActionToOpenDetails(reminderId) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        builder: NotificationCompat.Builder
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH;
        val notificationChannel = NotificationChannel(
            channelInfo.id,
            channelInfo.name,
            importance
        )
        notificationChannel.enableVibration(true)
        builder.setChannelId(channelInfo.id)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun NotificationCompat.Builder.addActionToOpenDetails(reminderId: String) {
        addAction(
            R.drawable.ic_launcher_background,
            context.getString(R.string.notification_action_label),
            pendingIntent(reminderId)
        )
    }

    private fun pendingIntent(requestId: String): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.reminderDetailsFragment)
            .setArguments(Bundle().apply { putRequestId(requestId) })
            .createPendingIntent()
    }

    private fun Bundle.putRequestId(requestId: String) {
        putString(context.getString(R.string.broadcast_reminder_request_id_extra), requestId)
    }
}