package com.example.trackingapp.di

import android.content.Context
import androidx.room.Room
import com.example.trackingapp.data.local.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provide the Database Class
    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext ctx: Context
    ): RunDatabase {
        return Room.databaseBuilder(
            ctx, RunDatabase::class.java, "runnning_db"
        ).build()
    }

    // Provide Dao class
    @Provides
    @Singleton
    fun provideDao(runDb: RunDatabase) = runDb.getDao()

}