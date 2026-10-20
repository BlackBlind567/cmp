package com.hybrid.internet.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.hybrid.internet.R

object AppNotificationChannels {
    const val DEFAULT_CHANNEL_ID = "hybrid_internet_alerts_v2"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.d(TAG, "Notification channel skipped: SDK=${Build.VERSION.SDK_INT}")
            return
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            DEFAULT_CHANNEL_ID,
            context.getString(R.string.default_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.default_notification_channel_name)
            enableVibration(true)
        }

        manager.createNotificationChannel(channel)
        Log.d(
            TAG,
            "Notification channel ready: id=$DEFAULT_CHANNEL_ID, importance=${channel.importance}"
        )
    }

    private const val TAG = "HybridFCM"
}
