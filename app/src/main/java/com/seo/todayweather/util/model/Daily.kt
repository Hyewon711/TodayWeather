package com.seo.todayweather.util.model

data class Daily (
    var dt: Long,
    var temp: DailyTemp,
    var humidity: Int,
    var weather: ArrayList<DailyAndHourly>
)