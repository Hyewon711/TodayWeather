package com.seo.todayweather.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }
}