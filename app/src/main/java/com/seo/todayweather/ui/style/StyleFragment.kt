package com.seo.todayweather.ui.style

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.databinding.FragmentStyleBinding
import com.seo.todayweather.ui.adapter.StyleRecyclerAdapter
import com.seo.todayweather.util.common.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StyleFragment : BaseFragment<FragmentStyleBinding>(FragmentStyleBinding::inflate) {
    private val firestoreManager = FirestoreManager()
    private var postList = mutableListOf<StylePost>()

    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        writeStylePost()
//        getStylePost()
        with(binding.rvStyle) {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = StyleRecyclerAdapter(postList)
        }
    }

    private fun writeStylePost() {
        /* test */
        val samplePost1 = StylePost(name = "User1", title = "Post 1", content = "Content 1")
        val samplePost2 = StylePost(name = "User2", title = "Post 2", content = "Content 2")
        postList.addAll(listOf(samplePost1, samplePost2))

        val db = FirebaseFirestore.getInstance()
        val notesCollection = db.collection("stylePost")

        try {
            notesCollection.add(samplePost1)
            notesCollection.add(samplePost2)
            // 작성이 성공한 경우
            Log.d(TAG,"Note added successfully!")
        } catch (e: Exception) {
            // 작성이 실패한 경우
            Log.d(TAG,"Error adding note: $e")
        }
    }

    private fun getStylePost() {
        CoroutineScope(Dispatchers.Main).launch {
            postList = firestoreManager.getAllNotes()
        }
    }
}