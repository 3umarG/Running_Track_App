package com.example.trackingapp.data.repository

import com.example.trackingapp.data.local.RunDAO
import com.example.trackingapp.data.model.Run
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunRepository(private val dao: RunDAO) {
    suspend fun insertRun(run: Run) = dao.insertRun(run)

    suspend fun deleteRun(run: Run) = dao.deleteRun(run)

    fun getAllRunsSortedByTimeStamp() = dao.getAllRunsSortedByTimeStamp()

    fun getAllRunsSortedByAvgSpeed() = dao.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByDistanceInMt() = dao.getAllRunsSortedByDistanceInMt()

    fun getAllRunsSortedByTotalTakeTimeInMill() = dao.getAllRunsSortedByTotalTakeTimeInMill()

    fun getAllRunsSortedByTotalCaloriesBurned() = dao.getAllRunsSortedByTotalCaloriesBurned()

    fun getTotalTimeInMillis() = dao.getTotalTimeInMillis()

    fun getTotalCaloriesBurned() = dao.getTotalCaloriesBurned()

    fun getTotalDistance() = dao.getTotalDistance()

    fun getTotalAvgSpeed() = dao.getTotalAvgSpeed()
}