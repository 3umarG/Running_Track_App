package com.example.trackingapp.ui.base.services

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import com.example.trackingapp.di.Constants
import timber.log.Timber

class TrackingService : LifecycleService() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action!!) {
            Constants.SERVICE_ACTION_START_OR_RESUME -> {
                Timber.d("Start or Resume Service")
                Toast.makeText(this, "Start Service", Toast.LENGTH_LONG).show()
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
}