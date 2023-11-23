package com.seo.todayweather.ui.style

import androidx.recyclerview.widget.GridLayoutManager
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.Post
import com.seo.todayweather.databinding.FragmentStyleBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter

class StyleFragment : BaseFragment<FragmentStyleBinding>(FragmentStyleBinding::inflate) {
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding.rvStyle) {
            val items = listOf(
                Post("Widget 1", "Content 1"),
                Post("Widget 2", "Content 2"),
                Post("Widget 3", "Content 3")
            )
            layoutManager = GridLayoutManager(activity, 2)
            adapter = StyleRecyclerAdapter(items)
        }
    }
}