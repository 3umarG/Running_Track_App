package com.example.trackingapp.ui.base.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.trackingapp.R
import com.example.trackingapp.ui.MainActivity
import com.example.trackingapp.utils.Constants
import com.example.trackingapp.utils.Constants.ACTION_GO_TO_MAIN_ACTIVITY
import com.example.trackingapp.utils.Constants.NOTIFICATION_ID
import timber.log.Timber

class TrackingService : LifecycleService() {
    var isFirstStarted = true
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action!!) {
            Constants.SERVICE_ACTION_START_OR_RESUME -> {
                if (isFirstStarted) {
                    startForegroundService()
                    isFirstStarted = false
                    Toast.makeText(this, "Start New Service", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Resume Service", Toast.LENGTH_LONG).show()
                }
            }
            Constants.SERVICE_ACTION_STOP -> {
                Timber.d("Stop Service")
            }
            Constants.SERVICE_ACTION_PAUSE -> {
                Timber.d("Pause Service")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForegroundService() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder =
            NotificationCompat
                .Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentTitle("Running Track App")
                .setContentText("00:00:00")
                .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    // This Pending Intent for Open the MainActivity when press the Notification
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(
            this, MainActivity::class.java
        ).also {
            it.action = ACTION_GO_TO_MAIN_ACTIVITY
        }, FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }
}