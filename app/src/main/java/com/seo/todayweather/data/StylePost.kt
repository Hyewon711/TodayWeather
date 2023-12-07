package com.seo.todayweather.data

data class StylePost(
    val id: Long = 0L,
    val userStyle: Int = 1,
    val name: String = "",
    val title: String = "",
    val content: String = "",
    val uri: String = "",
    val userUri: String = ""
)
