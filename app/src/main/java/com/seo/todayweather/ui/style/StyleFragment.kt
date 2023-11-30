package com.seo.todayweather.ui.style

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentStyleBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.viewmodel.StyleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StyleFragment : BaseFragment<FragmentStyleBinding>(FragmentStyleBinding::inflate) {
    private val viewModel: StyleViewModel by activityViewModels()

    override fun onViewCreated() {
        getStylePost()
        initView()
    }

    private fun initView() {
        getStylePost()
        with(binding) {
            /* StyleWriteFragment 이동 */
            lyToolbar.ivWrite.setOnAvoidDuplicateClick {
                styleFragment.changeFragment(this@StyleFragment, StyleWriteFragment())
            }
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