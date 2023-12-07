package com.seo.todayweather.ui.style

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentStyleWriteBinding
import com.seo.todayweather.util.common.PrefManager
import com.seo.todayweather.util.common.TAG
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.viewmodel.StyleViewModel

class StyleWriteFragment :
    BaseFragment<FragmentStyleWriteBinding>(FragmentStyleWriteBinding::inflate) {
    private val viewModel: StyleViewModel by activityViewModels()
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            /* StyleFragment로 이동 */
            ivBack.setOnAvoidDuplicateClick {
                styleWriteFragment.changeFragment(this@StyleWriteFragment, StyleFragment())
            }

            ivWrite.setOnAvoidDuplicateClick {
                val id = 1
                val userStyle = PrefManager.getInstance().selectStyle
                val name = "서혜원"
                val title = inputTitleBody.text.toString()
                val contents = inputContentsBody.text.toString()
                viewModel.writeStylePost(id, userStyle, name, title, contents)
                Log.d(TAG, "initView: $id, $name, $title, $contents")
                Toast.makeText(requireContext(), "게시글을 등록하였습니다.", Toast.LENGTH_SHORT).show()
                styleWriteFragment.changeFragment(this@StyleWriteFragment, StyleFragment())

            }
        }
    }
}