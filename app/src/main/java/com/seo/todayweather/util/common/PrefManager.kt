package com.seo.todayweather.util.common

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val IS_LOCATION = "is_location"

class PrefManager {
    companion object {
        private lateinit var manager: PrefManager
        private lateinit var sp: SharedPreferences
        private lateinit var spEditor: SharedPreferences.Editor

        @SuppressLint("CommitPrefEdits")
        fun getInstance(): PrefManager {
            if (this::manager.isInitialized) {
                return manager
            } else {
                sp = PreferenceManager.getDefaultSharedPreferences(
                    ProjectApplication.getInstance())
                spEditor = sp.edit()
                manager = PrefManager()
            }
            return manager
        }
    }

    /**
     * 본 앱의 퍼미션 체크 여부
     */
    var isPermission : Boolean
        get() = sp.getBoolean(IS_LOCATION, false)
        set(permissionCheck) {
            with(spEditor){
                putBoolean(IS_LOCATION, permissionCheck).apply()
            }
        }
}