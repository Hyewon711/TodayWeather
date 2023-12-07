package com.seo.todayweather.util.common

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seo.todayweather.data.SelectChip
import io.grpc.internal.JsonUtil.getString
import okhttp3.internal.cache2.Relay.Companion.edit
import java.lang.reflect.Type

private const val IS_LOCATION = "is_location"
private const val IS_TTS = "is_tts"
private const val USER_STYLE = "user_style"
private const val USER_NAME = "user_name"
private const val USER_IMAGE = "user_image"
private const val USER_ID = "user_id"
private const val SELECT_CHIP_LIST = "select_chip_list"

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
                    ProjectApplication.getInstance()
                )
                spEditor = sp.edit()
                manager = PrefManager()
            }
            return manager
        }
    }

    /**
     * 본 앱의 퍼미션 체크 여부
     */
    var isPermission: Boolean
        get() = sp.getBoolean(IS_LOCATION, false)
        set(permissionCheck) {
            with(spEditor) {
                putBoolean(IS_LOCATION, permissionCheck).apply()
            }
        }

    /**
     * 현재 사용자의 지정 스타일
     */
    var selectStyle: Int
        get() = sp.getInt(USER_STYLE, 0)
        set(userSelect) {
            with(spEditor) {
                putInt(USER_STYLE, userSelect).apply()
            }
        }

    /**
     * 현재 사용자의 로그인 정보
     */
    var getUserId: Long
        get() = sp.getLong(USER_ID, 0L)
        set(userId) {
            with(spEditor) {
                putLong(USER_ID, userId).apply()
            }
        }

    var getUserName: String?
        get() = sp.getString(USER_NAME, "")
        set(userName) {
            with(spEditor) {
                putString(USER_NAME, userName).apply()
            }
        }
    var getUserImage: String?
        get() = sp.getString(USER_IMAGE, "")
        set(userName) {
            with(spEditor) {
                putString(USER_IMAGE, userName).apply()
            }
        }

    /**
     * TTS Switch Check
     */
    var getTTS: Boolean
        get() = sp.getBoolean(IS_TTS, true)
        set(ttsCheck) {
            with(spEditor) {
                putBoolean(IS_TTS, ttsCheck).apply()
            }
        }

    private val _selectChipListLiveData = MutableLiveData<List<SelectChip>>()
    val selectChipListLiveData: LiveData<List<SelectChip>> = _selectChipListLiveData


    val selectChipList: LiveData<List<SelectChip>>
        get() {
            val gson = Gson()
            val json = sp.getString(SELECT_CHIP_LIST, null)
            val type: Type = object : TypeToken<List<SelectChip>>() {}.type
            _selectChipListLiveData.value = gson.fromJson(json, type) ?: emptyList()
            return selectChipListLiveData
        }
    fun setSelectChipList(chipList: List<SelectChip>) {
        with(spEditor) {
            putString(SELECT_CHIP_LIST, Gson().toJson(chipList)).apply()
        }
        _selectChipListLiveData.value = chipList
    }

}