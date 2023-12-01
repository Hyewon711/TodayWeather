package com.seo.todayweather.util.common

object CurrentTemp {
    private val listeners = mutableListOf<(Int) -> Unit>()
    private var _temp: Int? = 0


    var temp: Int
        get() = _temp ?: 0
        set(value) {
            _temp = value
            notifyListeners(_temp)
        }

    fun addLocationChangeListener(listener: (Int) -> Unit) {
        listeners.add(listener)
        // 추가된 리스너에게 현재 값을 전달
        temp.let { listener.invoke(it) }
    }

    fun removeLocationChangeListener(listener: (Int) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners(temp: Int?) {
        listeners.forEach {
            if (temp != null) {
                it.invoke(temp)
            }
        }
    }
}