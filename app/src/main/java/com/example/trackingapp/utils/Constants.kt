package com.example.trackingapp.utils

import android.graphics.Color

object Constants {
    const val PERMISSION_REQUEST_CODE = 0

    const val SERVICE_ACTION_START_OR_RESUME = "SERVICE_ACTION_START_OR_RESUME"
    const val SERVICE_ACTION_PAUSE = "SERVICE_ACTION_PAUSE"
    const val SERVICE_ACTION_STOP = "SERVICE_ACTION_STOP"
    const val ACTION_GO_TO_MAIN_ACTIVITY = "ACTION_GO_TO_MAIN_ACTIVITY"

    const val LOCATION_REQUEST_INTERVALS = 5000L
    const val FASTEST_LOCATION_INTERVALS = 2000L

    const val POLY_LINE_COLOR = Color.GREEN
    const val POLY_LINE_WIDTH = 8f
    const val CAMERA_ZOOM = 15f

    const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1
}