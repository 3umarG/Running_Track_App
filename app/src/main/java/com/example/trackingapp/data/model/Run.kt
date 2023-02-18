package com.example.trackingapp.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs_record")
data class Run(
    var img: Bitmap? = null,
    var timeStamp: Long? = 0L,
    var avgSpeedInKM: Float? = 0f,
    var distanceInMt: Int? = 0,
    var totalTakeTimeInMill: Long? = 0L,
    var totalCaloriesBurned: Int? = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}