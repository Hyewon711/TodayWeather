package com.seo.todayweather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(private val locationManager: LocationManager) : ViewModel() {

    init {
        // 위치 업데이트 Flow를 수신하고 처리
        viewModelScope.launch(Dispatchers.Main) {
            locationManager.locationFlow().collect { location ->
                // 수신한 현재 위치 처리
                Log.d("로그", "업데이트 된 location ${location.latitude}, ${location.longitude}")
            }
        }
    }
}