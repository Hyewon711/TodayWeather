package com.seo.todayweather.repository

import retrofit2.Response
import com.seo.todayweather.util.api.WeatherApi
import javax.inject.Inject
import javax.inject.Singleton
import com.seo.todayweather.util.model.Weather

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {

    suspend fun getWeather(
        dataType : String, numOfRows : Int, pageNo : Int,
        baseDate : Int, baseTime : Int, nx : String, ny : String) : Response<Weather> {
        return weatherApi.getWeather(dataType,numOfRows,pageNo,baseDate,baseTime,nx,ny)
    }

}