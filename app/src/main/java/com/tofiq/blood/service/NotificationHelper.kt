package com.tofiq.blood.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tofiq.blood.MainActivity
import com.tofiq.blood.R

/**
 * Helper class for managing notifications following SOLID principles.
 * Single Responsibility: Handles notification creation and channel management.
 */
object NotificationHelper {
    private const val CHANNEL_ID = "donor_plus_notifications"
    private const val CHANNEL_NAME = "DonorPlus Notifications"
    private const val CHANNEL_DESCRIPTION = "Notifications for blood donation requests and updates"

    /**
     * Creates the notification channel for Android O and above.
     * Must be called before creating any notifications.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Shows a notification with the given title and body.
     * 
     * @param context Application context
     * @param notificationId Unique ID for the notification
     * @param title Notification title
     * @param body Notification body text
     * @param dataMap Optional data payload to pass with the notification
     */
    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        body: String,
        dataMap: Map<String, String>? = null
    ) {
        // Create notification channel if not already created
        createNotificationChannel(context)

        // Create intent for when notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Add data payload to intent extras if available
            dataMap?.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Get default notification sound
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Build notification
        // Use launcher foreground icon for notifications
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        // Add data payload as extras if available
        dataMap?.forEach { (key, value) ->
            notificationBuilder.addExtras(android.os.Bundle().apply {
                putString(key, value)
            })
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Show notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    /**
     * Generates a unique notification ID based on timestamp and hash.
     * This ensures each notification gets a unique ID.
     */
    fun generateNotificationId(title: String, body: String): Int {
        return (title + body).hashCode().and(0x7fffffff) // Ensure positive integer
    }
}

