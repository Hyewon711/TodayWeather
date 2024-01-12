package com.seo.todayweather.util.model

data class HourlyAndCurrent (
    var dt: Long,
    var temp: Double,
    var feels_like: Double,
    var uvi: Double,
    var humidity: Int,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Double,
    var weather: ArrayList<DailyAndHourly>
)