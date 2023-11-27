package com.seo.todayweather.remote.api

import com.seo.todayweather.remote.model.OpenWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET("data/2.5/onecall")
    fun getWeatherList(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<OpenWeather>
}