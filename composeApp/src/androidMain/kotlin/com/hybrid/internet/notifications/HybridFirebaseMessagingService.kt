package com.hybrid.internet.notifications

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hybrid.internet.MainActivity
import com.hybrid.internet.R
import com.hybrid.internet.core.notification.savePushToken
import java.net.URL

class HybridFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Firebase messaging service created.")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putString(KEY_FCM_TOKEN, token)
            .apply()
        Log.d(TAG, "FCM token refreshed and cached: $token")
        println("FCM_TOKEN_REFRESHED=$token")
        savePushToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(
            TAG,
            "FCM received: from=${remoteMessage.from}, messageId=${remoteMessage.messageId}, " +
                "sentTime=${remoteMessage.sentTime}, priority=${remoteMessage.priority}, " +
                "notificationTitle=${remoteMessage.notification?.title}, " +
                "notificationBody=${remoteMessage.notification?.body}, data=${remoteMessage.data}"
        )

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: getString(R.string.app_name)
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: remoteMessage.data["message"]
            ?: ""
        val imageUrl = remoteMessage.notification?.imageUrl?.toString()
            ?: remoteMessage.data["image_url"]
            ?: remoteMessage.data["image"]
            ?: remoteMessage.data["big_image"]

        if (body.isNotBlank()) {
            Log.d(TAG, "FCM display payload resolved: title=$title, body=$body, imageUrl=$imageUrl")
            showNotification(
                title = title,
                body = body,
                data = remoteMessage.data,
                imageUrl = imageUrl
            )
        } else {
            Log.w(TAG, "FCM ignored: empty body/message. data=${remoteMessage.data}")
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.w(TAG, "Some pending FCM messages were deleted before delivery.")
    }

    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String>,
        imageUrl: String?
    ) {
        if (!canPostNotifications()) {
            Log.w(
                TAG,
                "Notification not displayed: POST_NOTIFICATIONS permission is not granted. " +
                    "title=$title, data=$data"
            )
            return
        }

        AppNotificationChannels.create(this)
        Log.d(TAG, "Preparing notification intent with data extras: $data")

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NOTIFICATION_TAP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bigPicture = imageUrl?.takeIf { it.isNotBlank() }?.let(::loadBitmap)
        val style = if (bigPicture != null) {
            NotificationCompat.BigPictureStyle()
                .bigPicture(bigPicture)
                .setBigContentTitle(title)
                .setSummaryText(body)
        } else {
            NotificationCompat.BigTextStyle().bigText(body)
        }

        val notification = NotificationCompat.Builder(this, AppNotificationChannels.DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setColor(ContextCompat.getColor(this, R.color.notification_color))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(style)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        getSystemService(NotificationManager::class.java)
            .notify(notificationId, notification)
        Log.d(
            TAG,
            "Notification posted: id=$notificationId, channel=${AppNotificationChannels.DEFAULT_CHANNEL_ID}, " +
                "title=$title, hasBigPicture=${bigPicture != null}"
        )
    }

    private fun loadBitmap(imageUrl: String): Bitmap? {
        return runCatching {
            Log.d(TAG, "Loading notification big image: $imageUrl")
            URL(imageUrl).openStream().use { input ->
                BitmapFactory.decodeStream(input)
            }
        }.onFailure { error ->
            Log.w(TAG, "Unable to load notification big image: $imageUrl", error)
        }.getOrNull()
    }

    private fun canPostNotifications(): Boolean {
        val canPost = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "Notification permission check: sdk=${Build.VERSION.SDK_INT}, canPost=$canPost")
        return canPost
    }

    companion object {
        private const val TAG = "HybridFCM"
        const val ACTION_NOTIFICATION_TAP = "com.hybrid.internet.NOTIFICATION_TAP"
        private const val PREFS_NAME = "hybrid_push"
        private const val KEY_FCM_TOKEN = "fcm_token"

        fun cachedToken(context: Context): String? {
            return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_FCM_TOKEN, null)
        }
    }
}
