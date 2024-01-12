package com.seo.todayweather.ui.mypage

import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kakao.sdk.user.UserApiClient
import com.seo.todayweather.R
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentMyPageBinding
import com.seo.todayweather.ui.LoginActivity
import com.seo.todayweather.util.common.PrefManager
import com.seo.todayweather.util.common.TAG

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
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

            btnLogout.setOnAvoidDuplicateClick {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    } else {
                        Log.e(TAG, "로그아웃 성공", error)
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
            switchTts.isChecked = PrefManager.getInstance().getTTS
            switchTts.setOnCheckedChangeListener { _, _ ->
                // Switch 상태 변경 시 SharedPreferences에 저장
                PrefManager.getInstance().getTTS = switchTts.isChecked
            }
            spinnerStyle.adapter = context?.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.artist_types,
                    android.R.layout.simple_list_item_1
                )
            }
        }
    }
}