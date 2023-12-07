package com.seo.todayweather.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.data.StylePost
import com.seo.todayweather.ui.style.FirestoreManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StyleRepository @Inject constructor() {
    suspend fun getAllStylePostsFlow(): Flow<List<StylePost>> = callbackFlow {
        val registration = FirestoreManager().styleCollectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Error handling
                close(exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val stylePosts = snapshot.documents.mapNotNull {
                    it.toObject(StylePost::class.java)
                }
                trySend(stylePosts)
            }
        }
        awaitClose { registration.remove() }
    }

    suspend fun writeStylePost(userId: Long, userStyle: Int, userName: String, title: String, contents: String, uri: String, userUri: String) {
        val inputData = StylePost(id = userId, userStyle = userStyle, name = userName, title = title, content = contents, uri = uri, userUri = userUri)

        try {
            val documentReference = FirestoreManager().styleCollectionRef.add(inputData).await()
            // 작성이 성공한 경우
            // documentReference.id를 활용하여 추가 작업이 필요한 경우에 사용할 수 있습니다.
        } catch (e: Exception) {
            // 작성이 실패한 경우
            throw e
        }
    }
}