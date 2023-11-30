package com.seo.todayweather.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface DailyDao {
    @Transaction
    @Query("SELECT * FROM daily")
    fun getDaily(): List<DailyEntity>

    @Insert
    fun insertDaily(vararg daily: DailyEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDaily(daily: DailyEntity)

    @Delete
    fun deleteDaily(daily: DailyEntity)
}