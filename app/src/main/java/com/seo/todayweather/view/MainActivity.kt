package com.seo.todayweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.seo.todayweather.R
import com.seo.todayweather.databinding.ActivityMainBinding
import com.seo.todayweather.util.common.CUURRENTFRAGMENTTAG
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.view.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragmentTag: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(R.layout.activity_main)
        }
        initView()
        // 화면이 최초 실행 되었을 때 Fragment Manager를 호출
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(binding.container.id, HomeFragment(), HOME)
                .commitAllowingStateLoss()
            currentFragmentTag = HOME // 현재 보고 있는 fragment의 Tag
        }
    }

    // 화면을 회전해도 새로운 fragment를 생산하지 않고 현재 보고 있는 fragment를 불러오기 위해 tag를 저장한다.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CUURRENTFRAGMENTTAG, currentFragmentTag)
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}