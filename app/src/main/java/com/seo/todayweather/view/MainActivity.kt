package com.seo.todayweather.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
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
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragmentTag: String

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
//            setSupportActionBar(lyToolbar)
//            supportActionBar?.apply {
//                setDisplayHomeAsUpEnabled(true)
//                setHomeAsUpIndicator(R.drawable.ic_menu) // 햄버거 아이콘 이미지 설정
//            }
            navView.setNavigationItemSelectedListener(this@MainActivity)
            lyToolbar.ivMenu.setOnClickListener {
                Toast.makeText(this@MainActivity,"버튼",Toast.LENGTH_LONG)
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 액션바(Toolbar)의 아이템 클릭 이벤트 처리
        return when (item.itemId) {
            R.id.iv_menu -> {
                // 햄버거 버튼 클릭 시 Drawer 열고 닫기 처리
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> changeFragment(HOME, HomeFragment())
            R.id.nav_commend -> changeFragment(COMMEND, CommendFragment())
            R.id.nav_style -> changeFragment(STYLE, StyleFragment())
            R.id.nav_mypage -> changeFragment(MYPAGE, MyPageFragment())
        }
        return false
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