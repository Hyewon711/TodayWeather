package com.seo.todayweather.ui.style

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.seo.todayweather.data.StylePost
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreManager {
    val db = FirebaseFirestore.getInstance()
    val styleCollectionRef = db.collection("style")
    val storage = FirebaseStorage.getInstance()

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
