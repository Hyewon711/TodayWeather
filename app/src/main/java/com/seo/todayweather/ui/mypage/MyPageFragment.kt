package com.seo.todayweather.ui.mypage

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentMyPageBinding
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.ui.home.HomeFragment
import com.seo.todayweather.util.common.PrefManager

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate){
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            val userName = PrefManager.getInstance().getUserName
            val userImage = PrefManager.getInstance().getUserImage
            tvUserName.text = userName
            Glide.with(this@MyPageFragment)
                .load(userImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
                .into(binding.ivUser)
        }
    }
}