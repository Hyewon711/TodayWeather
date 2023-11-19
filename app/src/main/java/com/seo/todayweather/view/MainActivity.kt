package com.seo.todayweather.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.seo.todayweather.R
import com.seo.todayweather.databinding.ActivityMainBinding
import com.seo.todayweather.util.common.CUURRENTFRAGMENTTAG
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.view.home.HomeFragment
import com.seo.todayweather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragmentTag: String

    // 뷰모델 생성
    private val viewModel by viewModels<WeatherViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(R.layout.activity_main)
        }

        // 화면이 최초 실행 되었을 때 Fragment Manager를 호출
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(binding.container.id, HomeFragment(), HOME)
                .commit()
            currentFragmentTag = HOME // 현재 보고 있는 fragment의 Tag
        }
        initView()

        viewModel.getWeather(
            "JSON", 14, 1,
            20231119, 1100, "63", "89"
        )

        viewModel.weatherResponse.observe(this) {
            if (it.body() != null) {
                Log.d(TAG, "${it.body()}")
                for (i in it.body()?.response!!.body.items.item) {
                    Log.d(TAG, "$i")
                }
            } else {
                Log.d(TAG, "onCreate: Null 반환")
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CUURRENTFRAGMENTTAG, currentFragmentTag)
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun changeFragment(tag: String, fragment: Fragment) {
        // supportFragmentManager에 "first"라는 Tag로 저장된 fragment 있는지 확인
        if (supportFragmentManager.findFragmentByTag(tag) == null) { // Tag가 없을 때 -> 없을 리가 없다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmentTag)!!)
                .add(binding.container.id, fragment, tag) // 피드백 : hide, add 대신 .replace
                .commitAllowingStateLoss() //피드백 : .commit()

        } else { // Tag가 있을 때
            // 먼저 currentFragmentTag에 저장된 '이전 fragment Tag'를 활용해 이전 fragment를 hide 시킨다.
            // supportFragmentManager에 저장된 "first"라는 Tag를 show 시킨다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmentTag)!!)
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commitAllowingStateLoss() // 피드백 : replace 로 바꾸기
        }
        // currentFragmentTag에 '현재 fragment Tag' "first"를 저장한다.
        currentFragmentTag = tag
    }

}