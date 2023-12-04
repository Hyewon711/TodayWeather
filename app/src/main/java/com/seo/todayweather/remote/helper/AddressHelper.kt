package com.seo.todayweather.remote.helper

import android.content.Context
import com.seo.todayweather.R
import com.seo.todayweather.remote.api.OpenWeatherAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class AddressHelper(context: Context) {
    val context: Context
    var retrofit: Retrofit
    var weatherAPI: OpenWeatherAPI

    init {
        var text = ""
        try {
            text = URLEncoder.encode("그린팩토리", "UTF-8");
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("검색어 인코딩 실패", e)
        }

        val apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text

        this.context = context
        this.retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.weatherAPI = retrofit.create(OpenWeatherAPI::class.java)
    }
}