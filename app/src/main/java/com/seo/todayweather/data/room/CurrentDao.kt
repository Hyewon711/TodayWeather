package com.seo.todayweather.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface CurrentDao {
    @Transaction
    @Query("SELECT * FROM current")
    fun getCurrent(): List<CurrentEntity>

    @Insert
    fun insertCurrent(vararg current: CurrentEntity)

    /** REPLACE :
     * OnConflict strategy constant to replace the old data and continue the transaction.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrent(current: CurrentEntity)

    @Delete
    fun deleteCurrent(current: CurrentEntity)
}