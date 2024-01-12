package com.seo.todayweather.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.seo.todayweather.R
import com.seo.todayweather.databinding.ActivityMainBinding
import com.seo.todayweather.ui.adapter.ViewPagerAdapter
import com.seo.todayweather.ui.commend.CommendFragment
import com.seo.todayweather.ui.home.HomeFragment
import com.seo.todayweather.ui.mypage.MyPageFragment
import com.seo.todayweather.ui.style.StyleFragment
import com.seo.todayweather.util.common.CurrentLocation
import com.seo.todayweather.ui.viewmodel.StyleViewModel
import com.seo.todayweather.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragmentTag: String
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val tabTitleArray = arrayOf(
        "HOME",
        "COMMEND",
        "STYLE",
        "MYPAGE"
    )

    // 뷰모델 생성
    private val weatherViewModel: WeatherViewModel by viewModels()
    lateinit var styleViewModel: StyleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViewPager()
        initView()

    }

    private fun getKMAApi() {
        weatherViewModel.getWeather(
            "JSON",
            2,
            1,
            20231207,
            1100,
            CurrentLocation.currentLocation.latitude.toInt().toString(),
            CurrentLocation.currentLocation.longitude.toInt().toString()
        )

        weatherViewModel.weatherResponse.observe(this) {
            if (it.body() != null) {
                Log.d(TAG, "${it.body()}")
                it.body()?.response!!.body.items.item.forEach { i ->
                    Log.d(TAG, "$i")
                }
            } else {
                Log.d(TAG, "onCreate: Null 반환")
            }
        }
    }


    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun initViewPager() {
        // Log.e("TAG", "entry")
        //ViewPager2 Adapter 셋팅
        viewPagerAdapter = ViewPagerAdapter(this@MainActivity)
        with(viewPagerAdapter) {

            addFragment(HomeFragment())
            addFragment(CommendFragment())
            addFragment(StyleFragment())
            addFragment(MyPageFragment())
        }

        with(binding.viewPager) {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = viewPagerAdapter
        }

    }
}
