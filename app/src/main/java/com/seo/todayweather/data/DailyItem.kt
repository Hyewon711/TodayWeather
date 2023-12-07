package com.seo.todayweather.data
data class DailyItem(
    var date: String,
    var imageUri: String,
    var minTemp: String,
    var maxTemp: String
)