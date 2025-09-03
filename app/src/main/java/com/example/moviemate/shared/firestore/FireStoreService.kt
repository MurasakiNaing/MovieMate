package com.example.moviemate.shared.firestore

import android.util.Log
import com.example.moviemate.shared.model.MovieCardPreview
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireStoreService {
    companion object {
        private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
        private val userId = FirebaseAuthService.getCurrentUser()?.uid
        private val userCollectionRef = db.collection("users").document(userId!!)

        fun createUserCollection(userId: String, email: String, onComplete: (() -> Unit)) {
            db.collection("users").document(userId)
                .set(mapOf("email" to email))
                .addOnSuccessListener {
                    Log.d("Firestore", "User email saved successfully")
                    onComplete.invoke()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error saving user email", e)
                    onComplete.invoke()
                }
        }

        suspend fun fetchUserFavorites(userId: String): List<MovieCardPreview> {
            return try {
                val snapshot = userCollectionRef
                    .collection("favorites")
                    .get()
                    .await()
                snapshot.toObjects(MovieCardPreview::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

        suspend fun fetchUserWatchlist(): List<MovieCardPreview> {
            return try {
                val snapshot = userCollectionRef.collection("watchlist").get().await()
                snapshot.toObjects(MovieCardPreview::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

        suspend fun toggleFavorite(movie: MovieCardPreview, isAdd: Boolean): Int {
            try {
                val favoritesRef = userCollectionRef.collection("favorites")
                if (isAdd) {
                    favoritesRef.document(movie.id.toString()).set(movie).await()
                    return 1
                } else {
                    favoritesRef.document(movie.id.toString()).delete().await()
                    return 0
                }
            } catch (e: Exception) {
                throw e
            }
        }

        suspend fun toggleWatchlist(movie: MovieCardPreview, isAdd: Boolean): Int {
            try {
                val watchlistRef = userCollectionRef.collection("watchlist")
                if (isAdd) {
                    watchlistRef.document(movie.id.toString()).set(movie).await()
                    return 1
                } else {
                    watchlistRef.document(movie.id.toString()).delete().await()
                    return 0
                }
            } catch (e: Exception) {
                throw e
            }
        }


        fun listenFavorites(onUpdate: (List<MovieCardPreview>) -> Unit) {
            userCollectionRef.collection("favorites")
                .addSnapshotListener { snapshot, error ->
                    error?.let { return@addSnapshotListener }
                    snapshot?.let {
                        val movies =
                            it.documents.mapNotNull { it.toObject(MovieCardPreview::class.java) }
                        onUpdate(movies)
                    }
                }
        }

        fun listenWatchlist(onUpdate: (List<MovieCardPreview>) -> Unit) {
            userCollectionRef.collection("watchlist")
                .addSnapshotListener { snapshot, error ->
                    error?.let { return@addSnapshotListener }
                    snapshot?.let {
                        val movies =
                            it.documents.mapNotNull { it.toObject(MovieCardPreview::class.java) }
                        onUpdate(movies)
                    }
                }
        }

    }

}