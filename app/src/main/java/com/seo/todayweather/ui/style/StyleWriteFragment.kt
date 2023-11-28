package com.seo.todayweather.ui.style

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentStyleWriteBinding
import com.seo.todayweather.util.common.TAG
import com.seo.todayweather.util.extension.changeFragment

class StyleWriteFragment :
    BaseFragment<FragmentStyleWriteBinding>(FragmentStyleWriteBinding::inflate) {
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
                val name = "서혜원"
                val title = inputTitleBody.text.toString()
                val contents = inputContentsBody.text.toString()
                writeStylePost(id, name, title, contents)
                Log.d(TAG, "initView: $id, $name, $title, $contents")
            }
        }
    }

    private fun writeStylePost(userId: Int, userName: String, title: String, contents: String) {
        /* test */
        val inputData = StylePost(id = userId, name = userName, title = title, content = contents)
        try {
            val db = FirebaseFirestore.getInstance()
            val stylePostCollection = db.collection("stylePost")
            stylePostCollection.add(inputData).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            // 작성이 성공한 경우
            Log.d(TAG, "stylePostCollection added successfully")
            Toast.makeText(context, "작성을 완료하였습니다", Toast.LENGTH_SHORT).show()
            with(binding) {
                styleWriteFragment.changeFragment(this@StyleWriteFragment, StyleFragment())
            }
        } catch (e: Exception) {
            // 작성이 실패한 경우
            Log.d(TAG, "Error adding note: $e")
        }
    }
}