package com.hybrid.internet.update

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateManager(
    private val activity: Activity,
    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest>
) {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(activity, "Update downloaded. Restarting app...", Toast.LENGTH_LONG).show()
            appUpdateManager.completeUpdate()
        }
    }

    fun checkForUpdate() {
        appUpdateManager.registerListener(installStateUpdatedListener)

        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                when {
                    shouldStartImmediateUpdate(appUpdateInfo) -> {
                        startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
                    }

                    shouldStartFlexibleUpdate(appUpdateInfo) -> {
                        startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE)
                    }

                    appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED -> {
                        appUpdateManager.completeUpdate()
                    }
                }
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "Unable to check app update availability.", error)
            }
    }

    fun resumeUpdateIfNeeded() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                when {
                    appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                        startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
                    }

                    appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED -> {
                        appUpdateManager.completeUpdate()
                    }
                }
            }
    }

    fun onUpdateResult(resultCode: Int) {
        if (resultCode != Activity.RESULT_OK) {
            val resultName = when (resultCode) {
                Activity.RESULT_CANCELED -> "canceled"
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> "failed"
                else -> "code=$resultCode"
            }
            Log.w(TAG, "In-app update flow did not complete: $resultName")
        }
    }

    fun release() {
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    private fun shouldStartImmediateUpdate(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
            appUpdateInfo.updatePriority() >= HIGH_PRIORITY_UPDATE &&
            appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
    }

    private fun shouldStartFlexibleUpdate(appUpdateInfo: AppUpdateInfo): Boolean {
        val stalenessDays = appUpdateInfo.clientVersionStalenessDays() ?: 0
        return appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
            stalenessDays >= FLEXIBLE_UPDATE_STALENESS_DAYS &&
            appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo, updateType: Int) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            updateLauncher,
            AppUpdateOptions.newBuilder(updateType).build()
        )
    }

    companion object {
        private const val TAG = "InAppUpdate"
        private const val HIGH_PRIORITY_UPDATE = 4
        private const val FLEXIBLE_UPDATE_STALENESS_DAYS = 1
    }
}
