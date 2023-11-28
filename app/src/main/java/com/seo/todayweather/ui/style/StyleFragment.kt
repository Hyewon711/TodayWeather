package com.seo.todayweather.ui.style

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentStyleBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.util.common.TAG
import com.seo.todayweather.util.extension.changeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StyleFragment : BaseFragment<FragmentStyleBinding>(FragmentStyleBinding::inflate) {
    private val firestoreManager = FirestoreManager()
    private var postList = mutableListOf<StylePost>()

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
        CoroutineScope(Dispatchers.Main).launch {
            postList = firestoreManager.getAllStylePosts()
            with(binding.rvStyle) {
                layoutManager = GridLayoutManager(activity, 2)
                adapter = StyleRecyclerAdapter(postList)
            }
        }
    }
}