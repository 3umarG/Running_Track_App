package com.example.trackingapp.ui.base.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.trackingapp.R
import com.example.trackingapp.ui.MainActivity
import com.example.trackingapp.utils.Constants
import com.example.trackingapp.utils.Constants.ACTION_GO_TO_MAIN_ACTIVITY
import com.example.trackingapp.utils.Constants.FASTEST_LOCATION_INTERVALS
import com.example.trackingapp.utils.Constants.LOCATION_REQUEST_INTERVALS
import com.example.trackingapp.utils.Constants.NOTIFICATION_ID
import com.example.trackingapp.utils.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

/***
 * Single Polyline : List of Coordinates Connected with each others to draw the line
 * The Whole Path : One or more Single Polyline = List<List<Coordinates>>
 */
typealias PolyLinePoints = MutableList<LatLng>
typealias PolyLinesPoints = MutableList<PolyLinePoints>

class TrackingService : LifecycleService() {
    private var isFirstStarted = true
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        /***
         * Make a live data objects to observe on it at the fragment and to change these values
         * based on the location .
         *
         * we can use Broadcast Receivers but this way more easy and can handle.
         */
        val isStartTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLinesPoints>()
    }

    /***
     * at the start of my service i want to post these initial values to live data.
     */
    private fun postInitialValues() {
        isStartTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isStartTracking.observe(this) {
            updateLocationTracking(it)
        }
    }

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

    /***
     * for every new path on start/resume the track and the service
     * you must add new emptyMutableList to add the coordinates later on it.
     *
     * I have two cases :
     *  1- if this the first time to start tracking , so the poly lines will be null --> insert mutableListOf(mutableListOf())
     *  2- if there is already at least one path , you insert new mutableList() only for new line.
     */
    private fun addNewEmptyPolyline() {
        pathPoints.value?.apply {
            add(mutableListOf())
            // after adding the new empty polyline you submit the new value to the live data object.
            pathPoints.postValue(this)
        } ?: pathPoints.postValue(mutableListOf(mutableListOf()))
        // if this the first time the value will be null so you should insert the first line list.
    }

    /***
     * I have already add new path list
     * So I should start to add new coordinates points to this list
     */

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(it.latitude, it.longitude)
            pathPoints.value?.apply {
                // Last edited Path Line
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    /***
     * Work as Client for receive the updated location information
     * that we use to insert it on our live data object
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            /**
             * There is a Case we want this check condition for it:
             * if we move from place to another (updated location) ,
             * but we stop the tracking service ( we don't want to track the location )
             */
            if (isStartTracking.value!!) {
                println("NEW FUCKING LOCATION ::: Start Collecting the Location Callback")
                result.locations.let { locations ->
                    if (locations.isEmpty()) {
                        println("NEW FUCKING LOCATION ::: No New Locations")
                    }
                    for (location in locations) {
                        addPathPoint(location)
                        println("NEW FUCKING LOCATION ::: ${location.latitude} , ${location.longitude}")
                    }
                }
            }
        }
    }

    /***
     * when we toggle the isTracking to true that mean we want to start track our location.
     */
    // TODO :
    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasTrackingPermissions(this)) {
                val locationRequest: com.google.android.gms.location.LocationRequest =
                    com.google.android.gms.location.LocationRequest.Builder(
                        LOCATION_REQUEST_INTERVALS
                    ).apply {
                            setWaitForAccurateLocation(false)
                            setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVALS)
                            setPriority(PRIORITY_HIGH_ACCURACY)
                        }
                        .build()

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback as LocationCallback,
                    Looper.getMainLooper()
                )
                println("NEW FUCKING LOCATION ::: request FusedLocation DONE !!!")
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            println("NEW FUCKING LOCATION ::: No request FusedLocation")

        }
    }

    private fun startForegroundService() {

        // Start Or Resume the Service
        addNewEmptyPolyline()
        isStartTracking.postValue(true)

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