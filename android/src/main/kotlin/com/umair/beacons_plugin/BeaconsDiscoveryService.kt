package com.umair.beacons_plugin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

class BeaconsDiscoveryService : Service() {

    private lateinit var beaconHelper: BeaconHelper

    companion object {

        @JvmStatic
        private val TAG = "BeaconsDiscoveryService"

        @JvmStatic
        private val NOTIFICATION_ID = TAG + "_ID"

        @JvmStatic
        private var serviceRunning = false

        @JvmStatic
        private var serviceNotificationTitle = "Beacons Service"

        @JvmStatic
        private var serviceNotificationContent = "Looking for nearby beacons"

        fun setForegroundServiceNotification(title: String, content: String) {
            serviceNotificationTitle = title
            serviceNotificationContent = content
        }
    }

    override fun onCreate() {
        super.onCreate()
        beaconHelper = BeaconHelper(this)

        BeaconsPlugin.messenger?.let {
            Log.i(TAG, "$TAG service running. Send Callback")
            BeaconsPlugin.registerWith(it, beaconHelper, this)
            BeaconsPlugin.sendBLEScannerReadyCallback()
            serviceRunning = true
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification("${TAG}_ID", TAG, "${TAG}::WAKE_LOCK", serviceNotificationTitle, serviceNotificationContent)
        acquireWakeLock(intent, "${TAG}::WAKE_LOCK")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "$TAG service stopped.")

        if (serviceRunning) {
            beaconHelper.stopMonitoringBeacons()
        }

        serviceRunning = false
    }
}
