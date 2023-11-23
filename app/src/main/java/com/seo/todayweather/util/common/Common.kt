package com.seo.todayweather.util.common

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

// Fragment Tag
const val HOME = "home"
const val COMMEND = "commend"
const val STYLE = "style"
const val MYPAGE = "mypage"

// 화면 회전시 번들에 저장하고 불러올 때 사용하는 KEY
const val CUURRENTFRAGMENTTAG = "currentfragment"

// Rx Event 에러 태그
const val RXERROR = "RX_ERROR"

// Rx Event 더블 클릭 간격 시간
const val CLICK_INTERVAL_TIME = 300L

// Rx Event 텍스트 완성 시간
const val INPUT_COMPLETE_TIME = 1000L

// Location
const val LBS_CHECK_TAG = "LBS_CHECK_TAG"
const val LBS_CHECK_CODE = 100

val defaultDispatcher = Dispatchers.Default
val ioDispatcher = Dispatchers.IO
val mainDispatcher = Dispatchers.Main

val defaultScope = CoroutineScope(defaultDispatcher)
val ioScope = CoroutineScope(ioDispatcher)
val mainScope = CoroutineScope(mainDispatcher)

typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T