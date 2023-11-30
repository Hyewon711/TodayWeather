package com.seo.todayweather.util.common

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.kakao.sdk.common.KakaoSdk
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

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

}