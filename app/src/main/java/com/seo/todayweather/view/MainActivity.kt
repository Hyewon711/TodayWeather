package com.seo.todayweather.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
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
        initNavigation()
    }

    // 화면을 회전해도 새로운 fragment를 생산하지 않고 현재 보고 있는 fragment를 불러오기 위해 tag를 저장한다.
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
//            setSupportActionBar(toolbar)
//            supportActionBar?.apply {
//                setDisplayHomeAsUpEnabled(true)
//                setHomeAsUpIndicator(R.drawable.ic_menu) // 메뉴 아이콘 설정
//            }

            tvTitle.setOnClickListener {
                Log.d(TAG, "initNavigation: 클릭")
                // 클릭 이벤트 처리
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }

            navigation.setNavigationItemSelectedListener(this@MainActivity)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawerToggle.onOptionsItemSelected(item)) {
                    return true
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
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
                .add(binding.container.id, fragment, tag)
                .commitAllowingStateLoss()

        } else { // Tag가 있을 때
            // 먼저 currentFragmentTag에 저장된 '이전 fragment Tag'를 활용해 이전 fragment를 hide 시킨다.
            // supportFragmentManager에 저장된 "first"라는 Tag를 show 시킨다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmentTag)!!)
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commitAllowingStateLoss()
        }
        // currentFragmentTag에 '현재 fragment Tag' "first"를 저장한다.
        currentFragmentTag = tag
    }

}