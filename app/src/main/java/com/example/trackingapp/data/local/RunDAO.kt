package com.example.trackingapp.data.local

import androidx.room.*
import com.example.trackingapp.data.model.Run
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDAO {

    // Insert Run Record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run : Run)

    // Delete Run Record
    @Delete
    suspend fun deleteRun(run : Run)


    // Query sort by all of the data members
    @Query("SELECT * FROM runs_record ORDER BY timeStamp DESC")
    fun getAllRunsSortedByTimeStamp() : Flow<List<Run>>

    @Query("SELECT * FROM runs_record ORDER BY avgSpeedInKM DESC")
    fun getAllRunsSortedByAvgSpeed() : Flow<List<Run>>

    @Query("SELECT * FROM runs_record ORDER BY distanceInMt DESC")
    fun getAllRunsSortedByDistanceInMt() : Flow<List<Run>>

    @Query("SELECT * FROM runs_record ORDER BY totalTakeTimeInMill DESC")
    fun getAllRunsSortedByTotalTakeTimeInMill() : Flow<List<Run>>

    @Query("SELECT * FROM runs_record ORDER BY totalCaloriesBurned DESC")
    fun getAllRunsSortedByTotalCaloriesBurned() : Flow<List<Run>>

    // Queries for Statics
    @Query("SELECT SUM(totalTakeTimeInMill) FROM runs_record")
    fun getTotalTimeInMillis(): Flow<Long>

    @Query("SELECT SUM(totalCaloriesBurned) FROM runs_record")
    fun getTotalCaloriesBurned(): Flow<Int>

    @Query("SELECT SUM(distanceInMt) FROM runs_record")
    fun getTotalDistance(): Flow<Int>

    @Query("SELECT AVG(avgSpeedInKM) FROM runs_record")
    fun getTotalAvgSpeed(): Flow<Float>


}