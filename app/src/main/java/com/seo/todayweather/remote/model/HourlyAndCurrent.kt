package com.seo.todayweather.remote.model

data class HourlyAndCurrent (
    var dt: Long,
    var temp: Double,
    var feels_like: Double,
    var uvi: Double,
    var humidity: Int,
    var clouds: Int,
    var wind_speed: Double,
    var visibility: Int,
    var weather: ArrayList<DailyAndHourly>
)