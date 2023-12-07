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
import com.seo.todayweather.viewmodel.StyleViewModel
import com.seo.todayweather.viewmodel.WeatherViewModel
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
//        binding = ActivityMainBinding.inflate(layoutInflater).also {
//            setContentView(R.layout.activity_main)
//        }
        // 화면이 최초 실행 되었을 때 Fragment Manager를 호출
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(binding.container.id, HomeFragment(), HOME)
//                .commit()
//            currentFragmentTag = HOME // 현재 보고 있는 fragment의 Tag
//        }
        initViewPager()
        initView()

    }

    private fun getKMAApi() {
        weatherViewModel.getWeather(
            "JSON",
            2,
            1,
            20231205,
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


//        Adapter 연결
//        with(binding.viewPager) {
//           this.adapter = viewPagerAdapter
//           this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                }
//            })
//        }
//
//        //ViewPager, TabLayout 연결
//        TabLayoutMediator(
//            binding.tabLayout,
//            binding.viewPager,
//            TabConfigurationStrategy { tab, position -> tab.text = position.toString() })
//            .attach()
    }
//    private fun changeFragment(tag: String, fragment: Fragment) {
//        // supportFragmentManager에 "first"라는 Tag로 저장된 fragment 있는지 확인
//        if (supportFragmentManager.findFragmentByTag(tag) == null) { // Tag가 없을 때 -> 없을 리가 없다.
//            supportFragmentManager
//                .beginTransaction()
//                .hide(supportFragmentManager.findFragmentByTag(currentFragmentTag)!!)
//                .add(binding.container.id, fragment, tag)
//                .commit()
//        } else { // Tag가 있을 때
//            // 먼저 currentFragmentTag에 저장된 '이전 fragment Tag'를 활용해 이전 fragment를 hide 시킨다.
//            // supportFragmentManager에 저장된 "first"라는 Tag를 show 시킨다.
//            supportFragmentManager
//                .beginTransaction()
//                .hide(supportFragmentManager.findFragmentByTag(currentFragmentTag)!!)
//                .show(supportFragmentManager.findFragmentByTag(tag)!!)
//                .commit()
//        }
//        // currentFragmentTag에 '현재 fragment Tag' "first"를 저장한다.
//        currentFragmentTag = tag
//    }

}
