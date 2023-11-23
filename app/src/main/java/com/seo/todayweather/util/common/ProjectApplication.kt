package com.seo.todayweather.util.common

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProjectApplication : Application() {
    companion object {
        private var instance: ProjectApplication? = null
        fun getInstance(): ProjectApplication {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        com.seo.todayweather.ui.LocationWorker.startLocationWork(this)
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
//    @SuppressLint("MissingPermission")
//    fun checkLocationCurrentDevice() {
//        /**
//         * FusedLocationProviderApi 에서
//         * 위치 업데이트를위한 서비스 품질등 다양한요청을
//         * 설정하는데 사용하는 데이터객체인
//         * LocationRequest 를 획득
//         */
//        val locationIntervalTime = 1000L
//        val priorityType = if (currentProvider.equals(LocationManager.GPS_PROVIDER, ignoreCase = true)) {
//            Priority.PRIORITY_HIGH_ACCURACY
//        } else {
//            //배터리와 정확도의 밸런스를 고려하여 위치정보를 획득(정확도 다소 높음)
//            Priority.PRIORITY_BALANCED_POWER_ACCURACY
//        }
//        mLocationRequest = com.google.android.gms.location.LocationRequest.Builder(priorityType, locationIntervalTime)
//            .setWaitForAccurateLocation(true)
//            .setMinUpdateIntervalMillis(locationIntervalTime)
//            .setIntervalMillis(3000.toLong()) //위치가 update 되는 주기
//            .setMaxUpdateDelayMillis(locationIntervalTime)
//            .build()
//
//        /**
//         * 위치서비스 설정 정보를 저장하기 위한 빌더객체획득
//         */
//        val builder = LocationSettingsRequest.Builder().apply {
//            /**
//             * 현재 위치정보 Setting 정보가 저장된 LocationRequest
//             * 객체를 등록
//             */
//            addLocationRequest(mLocationRequest)
//        }
//        /**
//         * 위치정보 요청을 수행하기 위해 단말기에서
//         * 관련 시스템 설정(Gps,Network)이 활성화되었는지 확인하는 클래스인
//         * SettingClient 를 획득한다
//         */
//        val mSettingsClient = LocationServices.getSettingsClient(this)
//
//        /**
//         * 위치 서비스 유형을 저장하고
//         * 위치 설정에도 사용하기위해
//         * LocationSettingsRequest 객체를 획득
//         */
//        val mLocationSettingsRequest = builder.build()
//        val locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
//
//        /**
//         * 현재 위치제공자(Provider)와 상호작용하는 진입점인
//         * FusedLocationProviderClient 객체를 획득
//         */
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        /**
//         * 정상적으로 위치정보가 설정되었다면
//         * 위치업데이트를 요구하고, 설정이 잘못되었다면
//         * onFailure 를 처리한다
//         */
//        with(locationResponse) {
//            addOnSuccessListener {
//                Toast.makeText(applicationContext, "위치 받기 성공", Toast.LENGTH_SHORT).show()
//                mFusedLocationClient.requestLocationUpdates(
//                    mLocationRequest,
//                    mLocationCallback,
//                    Looper.getMainLooper()
//                )
//            }
//            addOnFailureListener { e ->
//                val exception = e as ApiException
//                Toast.makeText(applicationContext, "위치 받기 실패: $exception", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
//
//    /**
//     * 위치 이벤트에 대한 콜백을 제공.
//     * 단말기위치정보가 update 되면 자동으로 호출
//     * Fused Location Provider API 에 등록된
//     * 위치알림을 수신하는 데 사용
//     */
//    private val mLocationCallback: LocationCallback = object : LocationCallback() {
//        /**
//         *  성공적으로 위치정보와 넘어왔을때를 동작하는 Call back 함수
//         */
//        override fun onLocationResult(locationResult: LocationResult) {
//            super.onLocationResult(locationResult)
//            mCurrentLocation = locationResult.locations[0]
//            mCurrentLocation.let {
//                // 위치 업데이트를 받았을 때 updateLocation 함수 호출
//                updateLocation(it)
//            }
//        }
//        /**
//         * 현재 콜백이 동작가능한지에 대한 여부
//         */
//        override fun onLocationAvailability(availability: LocationAvailability) {
//            val message = if(availability.isLocationAvailable){
//                "위치 정보 획득이 가능합니다"
//            }else{
//                "현재 위치 정보를 가져올 수 없네요! 잠시 후 다시 시도하세요"
//            }
//            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
//        }
//    }
}