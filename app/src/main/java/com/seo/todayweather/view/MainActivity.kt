package com.seo.todayweather.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.seo.todayweather.R
import com.seo.todayweather.databinding.ActivityMainBinding
import com.seo.todayweather.util.common.COMMEND
import com.seo.todayweather.util.common.CUURRENTFRAGMENTTAG
import com.seo.todayweather.util.common.HOME
import com.seo.todayweather.util.common.MYPAGE
import com.seo.todayweather.util.common.STYLE
import com.seo.todayweather.view.commend.CommendFragment
import com.seo.todayweather.view.home.HomeFragment
import com.seo.todayweather.view.mypage.MyPageFragment
import com.seo.todayweather.view.style.StyleFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val TAG: String = "로그"
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragmentTag: String
    private lateinit var drawerLayout: DrawerLayout

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
                .commitAllowingStateLoss()
            currentFragmentTag = HOME // 현재 보고 있는 fragment의 Tag
        }
        initView()
        initNavigation() // tab layout을 하단에서 동작할 수 있도록 변경하기 (?attr/actionBarSize, )
    }

    // 화면을 회전해도 새로운 fragment를 생산하지 않고 현재 보고 있는 fragment를 불러오기 위해 tag를 저장한다.
    // 피드백 : 세로모드 먼저 구현하고, 가로모드를 작업 (같이 작업하면 어려울 수 있음
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CUURRENTFRAGMENTTAG, currentFragmentTag)
    }

    private fun initView() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun initNavigation() {
        with(binding) {
            drawerLayout = lyDrawer
//            setSupportActionBar(lyToolbar.toolbar)
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
            val navController = navHostFragment.navController
//            val appBarConfiguration = AppBarConfiguration(navController.graph)
            navigation.setupWithNavController(navController)
//            setupActionBarWithNavController(navController, appBarConfiguration)
            navigation.setNavigationItemSelectedListener(this@MainActivity)

        }

    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.lyDrawer.isDrawerOpen(GravityCompat.START)) {
                    binding.lyDrawer.closeDrawer(GravityCompat.START)
                } else {
                    binding.lyDrawer.openDrawer(GravityCompat.START)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> changeFragment(HOME, HomeFragment())
            R.id.nav_commend -> {
                Log.d(TAG, "onNavigationItemSelected: COMMEND")
                changeFragment(COMMEND, CommendFragment())
            }

            R.id.nav_style -> changeFragment(STYLE, StyleFragment())
            R.id.nav_mypage -> changeFragment(MYPAGE, MyPageFragment())
        }
        return true
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