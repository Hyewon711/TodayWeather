package com.seo.todayweather.ui.style

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentStyleBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.ui.home.HomeFragment
import com.seo.todayweather.util.common.TAG
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.viewmodel.StyleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StyleFragment : BaseFragment<FragmentStyleBinding>(FragmentStyleBinding::inflate) {
    private val viewModel: StyleViewModel by activityViewModels()
    private lateinit var styleAdapter: StyleRecyclerAdapter
    override fun onViewCreated() {
        getStylePost()
        initView()
    }

    private fun initView() {
        with(binding) {
            /* HomeFragment로 이동 */
            lyToolbar.ivBack.setOnAvoidDuplicateClick {
                styleFragment.changeFragment(this@StyleFragment, HomeFragment())
            }
            /* StyleWriteFragment 이동 */
            lyToolbar.ivWrite.setOnAvoidDuplicateClick {
                styleFragment.changeFragment(this@StyleFragment, StyleWriteFragment())
            }
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        styleAdapter.setItems(viewModel.stylePosts.value)
                        when (tab.position) {
                            0 -> {
                                Log.d(TAG, "onTabSelected: stylePosts All Select")
                            }

                            else -> {
                                styleAdapter.filterByCategory(tab.position)
                            }
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Do nothing
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Do nothing
                }
            })
        }
    }

    private fun getStylePost() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stylePosts.collect {
                    with(binding.rvStyle) {
                        layoutManager = GridLayoutManager(activity, 2)
                        styleAdapter = StyleRecyclerAdapter(viewModel.stylePosts.value)
                        adapter = styleAdapter
                    }
                }
            }
        }
    }
}