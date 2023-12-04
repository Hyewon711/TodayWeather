package com.seo.todayweather.remote.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.seo.todayweather.data.room.CurrentDao
import com.seo.todayweather.data.room.CurrentEntity
import com.seo.todayweather.data.room.DailyDao
import com.seo.todayweather.data.room.DailyEntity
import com.seo.todayweather.data.room.HourlyDao
import com.seo.todayweather.data.room.HourlyEntity


@Database(entities = [CurrentEntity::class, HourlyEntity::class, DailyEntity::class], version = 2)
abstract class DatabaseHelper: RoomDatabase() {
    abstract fun currentDao(): CurrentDao
    abstract fun hourlyDao(): HourlyDao
    abstract fun dailyDao(): DailyDao

    companion object {
        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            instance?.let { return instance!! } ?: synchronized(DatabaseHelper::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "eachweather.db"
                ).build()
                return instance!!
            }
        }
    }
}