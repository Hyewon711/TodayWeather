package com.seo.todayweather.remote.model

data class Daily (
    var dt: Long,
    var temp: DailyTemp,
    var humidity: Int,
    var weather: ArrayList<DailyAndHourly>
)