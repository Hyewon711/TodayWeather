package com.seo.todayweather.ui.mypage

import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentMyPageBinding
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.ui.home.HomeFragment

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