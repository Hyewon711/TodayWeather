package com.seo.todayweather.ui.commend

import androidx.recyclerview.widget.GridLayoutManager
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentCommendBinding
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.ui.home.HomeFragment
import com.seo.todayweather.ui.mypage.MyPageFragment
import com.seo.todayweather.ui.style.StyleFragment

class CommendFragment : BaseFragment<FragmentCommendBinding>(FragmentCommendBinding::inflate) {
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            /* Home Fragment 이동 */
            lyCommend.setOnAvoidDuplicateClick {
                commendFragment.changeFragment(this@CommendFragment, StyleFragment())
            }
            lyCommendTomorrow.setOnAvoidDuplicateClick {
                commendFragment.changeFragment(this@CommendFragment, MyPageFragment())
            }
        }
        with(binding.rvStyle) {
            val items = listOf(
                StylePost("1", "SEO","Title1","Content1"),
                StylePost("2", "SEO","Title2","Content2"),
                StylePost("3", "SEO","Title3","Content3"),
                StylePost("4", "SEO","Title4","Content4"),
                StylePost("5", "SEO","Title5","Content5"),

                )
            layoutManager = GridLayoutManager(activity, 2)
            adapter = StyleRecyclerAdapter(items)
        }
    }
}