package com.seo.todayweather.view.home

import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            lyToolbar.setToolbarMenu("", false) {

            }

            tvMyLocation.setOnAvoidDuplicateClick {

            }
        }
    }
}