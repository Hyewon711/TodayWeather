package com.seo.todayweather.ui.commend

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentCommendBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.ui.mypage.MyPageFragment
import com.seo.todayweather.ui.style.StyleFragment
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.viewmodel.StyleViewModel
import kotlinx.coroutines.launch

class CommendFragment : BaseFragment<FragmentCommendBinding>(FragmentCommendBinding::inflate) {
    private val viewModel: StyleViewModel by activityViewModels()

    override fun onViewCreated() {
        getStylePost()
        initView()
    }

    private fun initView() {
        with(binding) {
            /* Style Fragment 이동 */
            lyCommend.setOnAvoidDuplicateClick {
                commendFragment.changeFragment(this@CommendFragment, StyleFragment())
            }
            lyCommendTomorrow.setOnAvoidDuplicateClick {
                commendFragment.changeFragment(this@CommendFragment, MyPageFragment())
            }
        }
        with(binding.rvStyle) {
            val items = listOf(
                StylePost(1, "SEO", "Title1", "Content1"),
                StylePost(2, "SEO", "Title2", "Content2"),
                StylePost(3, "SEO", "Title3", "Content3"),
                StylePost(4, "SEO", "Title4", "Content4"),
                StylePost(5, "SEO", "Title5", "Content5"),
                )
            layoutManager = GridLayoutManager(activity, 2)
            adapter = StyleRecyclerAdapter(items)
        }
    }

    private fun getStylePost() {
        var postList: List<StylePost>

        // StateFlow를 관찰하고 UI를 업데이트
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stylePosts.collect {
                    postList = it
                    with(binding.rvStyle) {
                        layoutManager = GridLayoutManager(activity, 2)
                        adapter = StyleRecyclerAdapter(postList)
                    }
                }
            }
        }
    }
}