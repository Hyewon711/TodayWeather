package com.seo.todayweather.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface HourlyDao {
    @Transaction
    @Query("SELECT * FROM hourly")
    fun getHourly(): List<HourlyEntity>

    @Insert
    fun insertHourly(vararg hourly: HourlyEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateHourly(hourly: HourlyEntity)

    @Delete
    fun deleteHourly(hourly: HourlyEntity)
}