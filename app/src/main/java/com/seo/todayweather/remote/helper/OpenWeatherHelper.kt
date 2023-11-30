package com.seo.todayweather.remote.helper

import android.content.Context
import android.util.Log
import com.seo.todayweather.BuildConfig
import com.seo.todayweather.R
import com.seo.todayweather.data.room.CurrentEntity
import com.seo.todayweather.data.room.DailyEntity
import com.seo.todayweather.data.room.HourlyEntity
import com.seo.todayweather.remote.api.OpenWeatherAPI
import com.seo.todayweather.remote.model.Daily
import com.seo.todayweather.remote.model.HourlyAndCurrent
import com.seo.todayweather.remote.model.OpenWeather
import com.seo.todayweather.util.common.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherHelper(context: Context) {
    val context: Context
    val databaseHelper: DatabaseHelper
    var retrofit: Retrofit
    var weatherAPI: OpenWeatherAPI

    init {
        this.context = context
        this.retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.weatherAPI = retrofit.create(OpenWeatherAPI::class.java)
        this.databaseHelper = DatabaseHelper.getInstance(context)
    }

    fun requestHourlyWeatherAPI(
        lat: String,
        lon: String,
        hourlyData: (ArrayList<HourlyAndCurrent>) -> Unit
    ) {
        weatherAPI.getWeatherList(
            lat,
            lon,
            context.getString(R.string.exclude_hourly),
            BuildConfig.OPEN_WEATHER_KEY,
            context.getString(R.string.units)
        )
            .enqueue(object : Callback<OpenWeather> {
                override fun onFailure(call: Call<OpenWeather>, t: Throwable) {
                    Log.d(TAG, "hourly 실패 : $t")
                }

                override fun onResponse(call: Call<OpenWeather>, response: Response<OpenWeather>) {
                    Log.d(
                        TAG,
                        "hourly 성공 : ${response.body()?.daily} / ${response.body()?.hourly} "
                    )
                    Log.d(TAG, "${response.raw()}")



                    response.body()?.let {
                        hourlyData(it.hourly!!)
                        for (i in 0..23) {
                            it.hourly!![i].let {
                                HourlyEntity(
                                    i + 101,
                                    it.dt,
                                    it.temp,
                                    it.feels_like,
                                    it.humidity,
                                    it.clouds,
                                    it.visibility,
                                    it.weather[0].id,
                                    it.weather[0].main,
                                    it.weather[0].description,
                                    it.weather[0].icon
                                ).let { entity ->
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.IO) {
                                            if (databaseHelper.hourlyDao().getHourly().size >= 24)
                                                databaseHelper.hourlyDao().updateHourly(entity)
                                            else
                                                databaseHelper.hourlyDao().insertHourly(entity)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }

    fun requestCurrentWeatherAPI(
        lat: String,
        lon: String,
        currentData: (HourlyAndCurrent) -> Unit
    ) {
        weatherAPI.getWeatherList(
            lat,
            lon,
            context.getString(R.string.exclude_current),
            BuildConfig.OPEN_WEATHER_KEY,
            context.getString(R.string.units)
        )
            .enqueue(object : Callback<OpenWeather> {
                override fun onFailure(call: Call<OpenWeather>, t: Throwable) {
                    Log.d(TAG, "OpenWeatherHelper - onFailure() called : $t")
                }

                override fun onResponse(call: Call<OpenWeather>, response: Response<OpenWeather>) {
                    Log.d(TAG, "current 성공 : ${response.body().toString()}")
                    Log.d(TAG, "${response.raw()}")
                    response.body()?.let {
                        currentData(it.current!!)
                        CurrentEntity(
                            101,
                            it.timezone,
                            it.current!!.dt,
                            it.current!!.temp,
                            it.current!!.feels_like,
                            it.current!!.humidity,
                            it.current!!.clouds,
                            it.current!!.visibility,
                            it.current!!.weather[0].id,
                            it.current!!.weather[0].main,
                            it.current!!.weather[0].description,
                            it.current!!.weather[0].icon
                        ).let {
                            CoroutineScope(Dispatchers.Main).launch {
                                withContext(Dispatchers.IO) {
                                    if (databaseHelper.currentDao().getCurrent().isNotEmpty())
                                        databaseHelper.currentDao().updateCurrent(it)
                                    else
                                        databaseHelper.currentDao().insertCurrent(it)
                                }
                            }
                        }
                    }
                }
            })
    }

    fun requestDailyWeatherAPI(
        lat: String,
        lon: String,
        dailyData: (ArrayList<Daily>) -> Unit
    ) {
        weatherAPI.getWeatherList(
            lat,
            lon,
            context.getString(R.string.exclude_daily),
            BuildConfig.OPEN_WEATHER_KEY,
            context.getString(R.string.units)
        )
            .enqueue(object : Callback<OpenWeather> {
                override fun onFailure(call: Call<OpenWeather>, t: Throwable) {
                    Log.d(TAG, "OpenWeatherHelper - onFailure() called : $t")
                }

                override fun onResponse(call: Call<OpenWeather>, response: Response<OpenWeather>) {
                    Log.d(TAG, "daily 성공 : ${response.body().toString()}")

                    response.body()?.let {
                        dailyData(it.daily!!)
                        for (i in 0..7) {
                            it.daily!![i].let {
                                DailyEntity(
                                    i + 101,
                                    it.dt,
                                    it.temp.day,
                                    it.temp.min,
                                    it.temp.max,
                                    it.humidity,
                                    it.weather[0].id,
                                    it.weather[0].main,
                                    it.weather[0].description,
                                    it.weather[0].icon
                                ).let { entity ->
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.IO) {
                                            if (databaseHelper.dailyDao().getDaily().size >= 8)
                                                databaseHelper.dailyDao().updateDaily(entity)
                                            else
                                                databaseHelper.dailyDao().insertDaily(entity)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}