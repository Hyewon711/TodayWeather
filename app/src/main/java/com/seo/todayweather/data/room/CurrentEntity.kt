package com.seo.todayweather.data.room

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "current")
data class CurrentEntity (
    @PrimaryKey(autoGenerate = true) var id: Int? = 0,
    @ColumnInfo var timezone: String = "",
    @ColumnInfo var dt: Long = 0L,
    @ColumnInfo var temp: Double = 0.0,
    @ColumnInfo var feelstemp: Double = 0.0,
    @ColumnInfo var humidity: Int = 0,
    @ColumnInfo var clouds: Int = 0,
    @ColumnInfo var visibility: Int = 0,
    @ColumnInfo var weatherid: Int = 0,
    @ColumnInfo var main: String = "",
    @ColumnInfo var description: String = "",
    @ColumnInfo var icon: String = ""
)