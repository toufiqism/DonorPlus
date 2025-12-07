package com.tofiq.blood.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase Cloud Messaging Service to handle incoming push notifications.
 * 
 * Handles both:
 * 1. Notification payloads - automatically displayed by system when app is in background
 * 2. Data payloads - always received, even when app is in foreground
 * 
 * Following SOLID principles:
 * - Single Responsibility: Handles FCM message processing
 * - Open/Closed: Extensible for different message types
 */
class DonorPlusMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "DonorPlusMessaging"
    }

    /**
     * Called when a new FCM token is generated.
     * This token should be sent to your server to enable push notifications for this device.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")
        
        // TODO: Send token to your server
        // Example: sendTokenToServer(token)
    }

    /**
     * Called when a message is received.
     * Handles both notification and data payloads.
     * 
     * Message types:
     * 1. Notification payload only: System handles display when app is in background
     * 2. Data payload only: Always handled here, app must display notification
     * 3. Both: System handles notification when in background, data always received here
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        // Check if message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataPayload(remoteMessage.data)
        }

        // Check if message contains notification payload
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message notification payload: title=${notification.title}, body=${notification.body}")
            handleNotificationPayload(notification, remoteMessage.data)
        }
    }

    /**
     * Handles data-only payloads.
     * Data payloads are always received, even when app is in foreground.
     * App must create and display the notification.
     */
    private fun handleDataPayload(data: Map<String, String>) {
        val title = data["title"] ?: "DonorPlus"
        val body = data["body"] ?: data["message"] ?: "New notification"
        
        // Generate unique notification ID
        val notificationId = NotificationHelper.generateNotificationId(title, body)
        
        // Show notification with data payload
        NotificationHelper.showNotification(
            context = applicationContext,
            notificationId = notificationId,
            title = title,
            body = body,
            dataMap = data
        )
    }

    /**
     * Handles notification payloads.
     * When app is in foreground, system doesn't auto-display notifications,
     * so we handle it manually here.
     * When app is in background, system displays it automatically,
     * but we still receive it here to process any data.
     */
    private fun handleNotificationPayload(
        notification: com.google.firebase.messaging.RemoteMessage.Notification,
        data: Map<String, String>
    ) {
        val title = notification.title ?: "DonorPlus"
        val body = notification.body ?: "New notification"
        
        // Generate unique notification ID
        val notificationId = NotificationHelper.generateNotificationId(title, body)
        
        // Show notification
        // Note: If app is in background, system already displayed it,
        // but we show it again to ensure it's visible and to handle data
        NotificationHelper.showNotification(
            context = applicationContext,
            notificationId = notificationId,
            title = title,
            body = body,
            dataMap = data
        )
    }
}
