package com.seo.todayweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.repository.StyleRepository
import com.seo.todayweather.ui.style.FirestoreManager
import com.seo.todayweather.util.common.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StyleViewModel @Inject constructor(private val repository: StyleRepository, val firestoreHelper: FirestoreManager) :
    ViewModel() {
    private val _stylePosts = MutableStateFlow<List<StylePost>>(emptyList())
    val stylePosts: MutableStateFlow<List<StylePost>> get() = _stylePosts

    private val _filteredData = MutableLiveData<List<StylePost>>()
    val filteredData: LiveData<List<StylePost>> get() = _filteredData

    init {
        // ViewModel이 생성될 때 Flow를 수집하도록 설정
        viewModelScope.launch {
            repository.getAllStylePostsFlow().collect { stylePosts ->
                _stylePosts.value = stylePosts
                Log.d(TAG, "가져온 데이터 $stylePosts")
                Log.d(TAG, "가져온 데이터 value 값 ${_stylePosts.value}")
            }
        }
    }

    fun writeStylePost(userId: Int, userStyle: Int, userName: String, title: String, contents: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.writeStylePost(userId, userStyle, userName, title, contents)
            } catch (e: Exception) {
                Log.d(TAG, "writeStylePost Exception : $e")
            }
        }
    }
}

