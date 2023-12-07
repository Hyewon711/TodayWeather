package com.seo.todayweather.util.common

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val IS_LOCATION = "is_location"
private const val USER_STYLE = "user_style"
private const val USER_NAME = "user_name"
private const val USER_IMAGE = "user_image"
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

    /**
     * 현재 사용자의 지정 스타일
     */
    var selectStyle : Int
        get() = sp.getInt(USER_STYLE, 0)
        set(userSelect) {
            with(spEditor) {
                putInt(USER_STYLE, userSelect).apply()
            }
        }

    /**
     * 현재 사용자의 로그인 정보
     */
    var getUserName : String?
        get() = sp.getString(USER_NAME,"")
        set(userName) {
            with(spEditor) {
                putString(USER_NAME, userName).apply()
            }
        }
    var getUserImage : String?
        get() = sp.getString(USER_IMAGE,"")
        set(userName) {
            with(spEditor) {
                putString(USER_IMAGE, userName).apply()
            }
        }
}