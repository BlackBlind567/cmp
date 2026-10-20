package com.hybrid.internet

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Build
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.hybrid.internet.app.App
import com.hybrid.internet.core.notification.NotificationNavigationBus
import com.hybrid.internet.core.notification.savePushToken
import com.hybrid.internet.notifications.AppNotificationChannels
import com.hybrid.internet.notifications.HybridFirebaseMessagingService
import com.hybrid.internet.update.InAppUpdateManager

class MainActivity : ComponentActivity() {
    private lateinit var inAppUpdateManager: InAppUpdateManager

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        inAppUpdateManager.onUpdateResult(result.resultCode)
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(TAG, "POST_NOTIFICATIONS permission granted=$isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity onCreate: intentExtras=${intent.extras?.keySet()?.associateWith { intent.extras?.get(it) }}")
        handleNotificationIntent(intent)

//        initKoin()
        AppNotificationChannels.create(this)
        requestNotificationPermissionIfNeeded()
        logFcmTokenIfAvailable()

        inAppUpdateManager = InAppUpdateManager(this, updateLauncher)
        inAppUpdateManager.checkForUpdate()

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.d(TAG, "MainActivity onNewIntent from notification: extras=${intent.extras?.keySet()?.associateWith { intent.extras?.get(it) }}")
        handleNotificationIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (::inAppUpdateManager.isInitialized) {
            inAppUpdateManager.resumeUpdateIfNeeded()
        }
    }

    override fun onDestroy() {
        if (::inAppUpdateManager.isInitialized) {
            inAppUpdateManager.release()
        }
        super.onDestroy()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Log.d(TAG, "POST_NOTIFICATIONS permission not required: sdk=${Build.VERSION.SDK_INT}")
            return
        }

        val isGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            Log.d(TAG, "Requesting POST_NOTIFICATIONS permission.")
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            Log.d(TAG, "POST_NOTIFICATIONS permission already granted.")
        }
    }

    private fun logFcmTokenIfAvailable() {
        Log.d(TAG, "Checking Firebase/FCM token availability.")
        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.w(TAG, "Firebase is not configured. Add composeApp/google-services.json to enable FCM.")
            return
        }

        HybridFirebaseMessagingService.cachedToken(this)?.let { cachedToken ->
            Log.d(TAG, "Cached FCM token: $cachedToken")
            println("FCM_TOKEN_CACHED=$cachedToken")
        }

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "Current FCM token: $token")
                println("FCM_TOKEN_CURRENT=$token")
                savePushToken(token)
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Unable to fetch FCM token.", error)
            }
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.action != HybridFirebaseMessagingService.ACTION_NOTIFICATION_TAP) return

        val ticketId = intent.getStringExtra("ticket_id")
        val type = intent.getStringExtra("type")
        Log.d(TAG, "Notification tap handled: type=$type, ticketId=$ticketId")

        if (!ticketId.isNullOrBlank()) {
            NotificationNavigationBus.openTickets(ticketId)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
