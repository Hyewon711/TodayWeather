package com.seo.todayweather.view.home

import androidx.recyclerview.widget.GridLayoutManager
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.SelectWidget
import com.seo.todayweather.databinding.FragmentHomeBinding
import com.seo.todayweather.view.adapter.HomeRecyclerAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
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
    }
}