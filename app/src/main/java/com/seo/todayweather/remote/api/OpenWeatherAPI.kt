package com.seo.todayweather.remote.api

import com.seo.todayweather.remote.model.OpenWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET("data/3.0/onecall")
    fun getWeatherList(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String
    ): Call<OpenWeather>
}