package com.seo.todayweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

class LocationManager(private val context: Context) {

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경될 때 호출되는 콜백
            updateLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            // 위치 제공자가 비활성화될 때 호출되는 콜백
        }

        override fun onProviderEnabled(provider: String) {
            // 위치 제공자가 활성화될 때 호출되는 콜백
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // 위치 제공자의 상태가 변경될 때 호출되는 콜백
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (hasLocationPermission()) {
            Log.d("로그", "startLocationUpdates: 업데이트 시작")
            // 위치 권한이 있는 경우 위치 업데이트 시작
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, // 최소 시간 간격(ms)
                0F, // 최소 거리 간격(m)
                locationListener
            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateLocation(location: Location) {
        // 위치 데이터 처리 (원하는 로직으로 교체)
        Log.d("로그", "updateLocation: ${location.latitude}, ${location.longitude}")
        updateLocationDetails(location)
    }

    private fun updateLocationDetails(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        Log.d("로그", "updateLocation: $addresses")
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val addressText = address.getAddressLine(0)
            Log.d("로그", "updateLocation: $addressText")
        } else {
            Log.d("로그", "주소를 찾을 수 없습니다")
        }
    }

    fun stopLocationUpdates() {
        // 위치 업데이트 중지
        locationManager.removeUpdates(locationListener)
    }

    // Flow를 통해 현재 위치를 주기적으로 내보내는 메서드
    fun locationFlow(): Flow<Location> = callbackFlow {
        // LocationListener를 사용하여 현재 위치를 수신
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Flow에 현재 위치를 내보냄
                trySend(location).isSuccess
                Log.d("로그", " locationFlow 현재위치 : $location")
            }

            override fun onProviderDisabled(provider: String) {
                // 위치 제공자가 비활성화될 때의 처리
            }

            override fun onProviderEnabled(provider: String) {
                // 위치 제공자가 활성화될 때의 처리
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 위치 제공자의 상태가 변경될 때의 처리
            }
        }

        @SuppressLint("MissingPermission")
        if (hasLocationPermission()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                90000, // 15분 간격
                0F,
                locationListener
            )
        }

        // Flow를 닫을 때 위치 업데이트 중지
        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }
}