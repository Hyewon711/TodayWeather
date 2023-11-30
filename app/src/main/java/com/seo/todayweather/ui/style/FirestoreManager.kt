package com.seo.todayweather.ui.style

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.data.StylePost
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()
    private val styleCollection: CollectionReference = db.collection("style")
    fun getAllStylePostsFlow(): Flow<List<StylePost>> = callbackFlow {
        val collectionRef = db.collection("style")

        val registration = collectionRef.addSnapshotListener { snapshot, exception ->
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

    fun getStylePostFlow(styleId: String): Flow<StylePost> = callbackFlow {
        val docRef: DocumentReference = db.collection("style").document(styleId)

        val registration = docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Error handling
                close(exception)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject(StylePost::class.java)!!)
            }
        }

        awaitClose { registration.remove() }
    }
}
