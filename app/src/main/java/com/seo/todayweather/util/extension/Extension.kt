package com.seo.todayweather.util.extension

import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.seo.todayweather.util.common.CLICK_INTERVAL_TIME
import com.seo.todayweather.util.common.mainScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
fun ViewGroup.changeFragment(from: Fragment, to: Fragment) {
    from.childFragmentManager
        .beginTransaction()
        .replace(this.id, to)
        .addToBackStack(null)
        .commit()
}

fun View.setOnAvoidDuplicateClickFlow(actionInMainThread: () -> Unit) {
    this.clicks()
        .flowOn(Dispatchers.Main) // 이후 chain의 메서드들은 쓰레드 io 영역에서 처리
        .throttleFirst(CLICK_INTERVAL_TIME)
        .flowOn(Dispatchers.IO) // 이후 chain의 메서드들은 쓰레드 main 영역에서 처리
        .onEach {// onEach{}를 main에서 실행
            actionInMainThread()
        }.launchIn(mainScope)
}

// throttleFirst()는 Flow에 없기 때문에 직접 구현해줘야 한다. debounce()는 있다.
private fun <T> Flow<T>.throttleFirst(intervalTime: Long): Flow<T> = flow {
    var throttleTime = 0L
    collect { upStream ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(upStream)
        }
    }
}
