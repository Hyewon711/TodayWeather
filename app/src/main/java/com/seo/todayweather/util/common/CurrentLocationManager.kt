package com.seo.todayweather.util.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import java.util.Locale


class CurrentLocationManager(private val context: Context) {

    /**
     * Fused Location Provider Api 에서
     * 위치 업데이트를위한 서비스 품질등 다양한요청을
     * 설정하는데 사용하는 객체.
     */
    private lateinit var mLocationRequest: LocationRequest

    /**
     * 현재 위치정보를 나타내는 객체
     */
    lateinit var mCurrentLocation: Location

    /**
     * 현재 위치제공자(Provider)와 상호작용하는 진입점
     */
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    /**
     * 현재 단말기에 설정된 위치 Provider
     */
    private var currentProvider = "provider"

    @SuppressLint("MissingPermission")
    fun checkLocationCurrentDevice() {
        /**
         * FusedLocationProviderApi 에서
         * 위치 업데이트를위한 서비스 품질등 다양한요청을
         * 설정하는데 사용하는 데이터객체인
         * LocationRequest 를 획득
         */
        val locationIntervalTime = 1000L
        val priorityType =
            if (currentProvider.equals(LocationManager.GPS_PROVIDER, ignoreCase = true)) {
                Priority.PRIORITY_HIGH_ACCURACY
            } else {
                //배터리와 정확도의 밸런스를 고려하여 위치정보를 획득(정확도 다소 높음)
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            }
        mLocationRequest = LocationRequest.Builder(priorityType, locationIntervalTime)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(locationIntervalTime)
            .setIntervalMillis(30000.toLong()) //위치가 update 되는 주기
            .setMaxUpdateDelayMillis(locationIntervalTime)
            .build()

        /**
         * 위치서비스 설정 정보를 저장하기 위한 빌더객체획득
         */
        val builder = LocationSettingsRequest.Builder().apply {
            /**
             * 현재 위치정보 Setting 정보가 저장된 LocationRequest
             * 객체를 등록
             */
            addLocationRequest(mLocationRequest)
        }

        /**
         * 위치정보 요청을 수행하기 위해 단말기에서
         * 관련 시스템 설정(Gps,Network)이 활성화되었는지 확인하는 클래스인
         * SettingClient 를 획득한다
         */
        val mSettingsClient = LocationServices.getSettingsClient(context)

        /**
         * 위치 서비스 유형을 저장하고
         * 위치 설정에도 사용하기위해
         * LocationSettingsRequest 객체를 획득
         */
        val mLocationSettingsRequest = builder.build()
        val locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest)

        /**
         * 현재 위치제공자(Provider)와 상호작용하는 진입점인
         * FusedLocationProviderClient 객체를 획득
         */
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        /**
         * 정상적으로 위치정보가 설정되었다면
         * 위치업데이트를 요구하고, 설정이 잘못되었다면
         * onFailure 를 처리한다
         */
        with(locationResponse) {
            addOnSuccessListener {
                Log.d(TAG, "위치 받기 성공")
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper()
                )
            }
            addOnFailureListener { e ->
                val exception = e as ApiException
                Log.d(TAG, "위치 받기 실패: $exception")
            }
        }
    }

    /**
     * 위치 이벤트에 대한 콜백을 제공.
     * 단말기위치정보가 update 되면 자동으로 호출
     * Fused Location Provider API 에 등록된
     * 위치알림을 수신하는 데 사용
     */
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        /**
         *  성공적으로 위치정보와 넘어왔을때를 동작하는 Call back 함수
         */
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            mCurrentLocation = locationResult.locations[0]
            mCurrentLocation.let {
                // 위치 업데이트를 받았을 때 updateLocation 함수 호출
                updateLocationDetails(it)
            }
        }

        /**
         * 현재 콜백이 동작가능한지에 대한 여부
         */
        override fun onLocationAvailability(availability: LocationAvailability) {
            val message = if (availability.isLocationAvailable) {
                "위치 정보 획득이 가능합니다"
            } else {
                "현재 위치 정보를 가져올 수 없네요! 잠시 후 다시 시도하세요"
            }
            Log.d(TAG, message)
        }
    }

    /* Geocoder를 이용하여 주소 변환 */
    private fun updateLocationDetails(location: Location) {
        CurrentLocation.currentLocation = location

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
}