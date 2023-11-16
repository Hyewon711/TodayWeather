package com.seo.todayweather.view.mypage

import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentMyPageBinding
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.view.home.HomeFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate){
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            ivUser.setOnAvoidDuplicateClick {
                mypageFragment.changeFragment(this@MyPageFragment, HomeFragment())
            }
        }
    }
}