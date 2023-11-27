package com.seo.todayweather.util.common

import android.location.Location

object CurrentLocation {
    private val listeners = mutableListOf<(Location) -> Unit>()
    private var _currentLocation: Location? = null

    var currentLocation: Location?
        get() = _currentLocation
        set(value) {
            _currentLocation = value
            notifyListeners(value)
        }

    fun addLocationChangeListener(listener: (Location) -> Unit) {
        listeners.add(listener)
        // 추가된 리스너에게 현재 값을 전달
        currentLocation?.let { listener.invoke(it) }
    }

    fun removeLocationChangeListener(listener: (Location) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners(location: Location?) {
        listeners.forEach { it.invoke(location ?: return@forEach) }
    }
}