package com.seo.todayweather.remote.model

data class DailyAndHourly (
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)