package com.seo.todayweather.ui.commend

import androidx.recyclerview.widget.GridLayoutManager
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.Post
import com.seo.todayweather.databinding.FragmentCommendBinding
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.ui.home.HomeFragment
import com.seo.todayweather.ui.mypage.MyPageFragment

class CommendFragment : BaseFragment<FragmentCommendBinding>(FragmentCommendBinding::inflate) {
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            /* Home Fragment 이동 */
            lyCommend.setOnAvoidDuplicateClick {
                commendFragment.changeFragment(this@CommendFragment, HomeFragment())
            }
            lyCommendTomorrow.setOnAvoidDuplicateClick {
                commendFragment.changeFragment(this@CommendFragment, MyPageFragment())
            }
        }
        with(binding.rvStyle) {
            val items = listOf(
                Post("Widget 1", "Content 1"),
                Post("Widget 2", "Content 2"),
                Post("Widget 3", "Content 3"),
                Post("Widget 4", "Content 4"),
                Post("Widget 5", "Content 5"),
                Post("Widget 6", "Content 6")
            )
            layoutManager = GridLayoutManager(activity, 2)
            adapter = StyleRecyclerAdapter(items)
        }
    }
}