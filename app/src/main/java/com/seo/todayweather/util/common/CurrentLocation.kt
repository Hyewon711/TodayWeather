package com.seo.todayweather.util.common

import android.location.Location

object CurrentLocation {
    private val listeners = mutableListOf<(Location, String?) -> Unit>()
    private var _currentLocation: Location? = null
    private var _addressText: String? = null
    var currentLocation: Location
        get() = _currentLocation as Location
        set(value) {
            _currentLocation = value
            notifyListeners(value, _addressText)
        }

    var addressText: String
        get() = _addressText ?: ""
        set(value) {
            _addressText = value
            notifyListeners(_currentLocation, value)
        }

    fun addLocationChangeListener(listener: (Location, String?) -> Unit) {
        listeners.add(listener)
        // 추가된 리스너에게 현재 값을 전달
        currentLocation.let { listener.invoke(it, _addressText) }
    }

    fun removeLocationChangeListener(listener: (Location, String?) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners(location: Location?, addressText: String?) {
        listeners.forEach { it.invoke(location ?: return@forEach, addressText) }
    }
}