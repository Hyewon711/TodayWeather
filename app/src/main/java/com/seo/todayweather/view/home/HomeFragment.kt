package com.seo.todayweather.view.home

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.SelectWidget
import com.seo.todayweather.databinding.FragmentHomeBinding
import com.seo.todayweather.view.adapter.HomeRecyclerAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    val TAG: String = "로그"
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding.rvHome) {
            val items = listOf(
                SelectWidget("Widget 1", "Content 1"),
                SelectWidget("Widget 2", "Content 2"),
                SelectWidget("Widget 3", "Content 3")
            )
            layoutManager = GridLayoutManager(activity, 2)
            adapter = HomeRecyclerAdapter(items)
        }
//
//        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
//        val navController = navHostFragment.navController
//        activity?.findViewById<NavigationView>(R.id.navigation)
//            ?.let { setupWithNavController(it, navController) }

        with(binding) {
            lyToolbar.ivMenu.setOnClickListener {
                activity?.findViewById<DrawerLayout>(R.id.ly_drawer)?.openDrawer(GravityCompat.START)
            }
        }
    }
}