package com.seo.todayweather.util.common

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class ProjectApplication : Application() {
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    companion object {
        private var instance: ProjectApplication? = null
        fun getInstance(): ProjectApplication {
            return instance!!
        }


    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSdk.init(this, "dcdc48f53693c6446d8532b7946975b7")

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }
        })
    }

    fun startLocationUpdates() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                updateLocation(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d("로그", "onStatusChanged: 상태 변경")
            }

            override fun onProviderEnabled(provider: String) {
                Log.d("로그", "onProviderEnabled: 공급자 활성화")
            }

            override fun onProviderDisabled(provider: String) {
                Log.d("로그", "onProviderDisabled: 공급자 비활성화")
            }
        }

        // getLastKnownLocation을 통해 최초의 위치 가져오기
        val lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            Log.d("로그", "getLastKnownLocation: $lastKnownLocation")
            updateLocation(lastKnownLocation)
        } else {
            Log.d("로그", "getLastKnownLocation: 이전 위치 없음")
        }

        // 위치 업데이트 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            0f,
            locationListener as LocationListener
        )
    }

    private fun updateLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        val geocoder = Geocoder(this, Locale.getDefault())
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
}