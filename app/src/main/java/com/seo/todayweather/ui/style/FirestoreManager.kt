package com.seo.todayweather.ui.style

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.seo.todayweather.data.StylePost
import kotlinx.coroutines.tasks.await

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val notesCollection: CollectionReference = db.collection("style")

    suspend fun addNote(note: StylePost) {
        notesCollection.add(note).await()
    }

    suspend fun getAllStylePosts(): MutableList<StylePost> {
        return notesCollection.get().await().toObjects(StylePost::class.java)
    }
}