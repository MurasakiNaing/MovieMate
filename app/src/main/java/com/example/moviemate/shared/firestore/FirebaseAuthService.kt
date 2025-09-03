package com.example.moviemate.shared.firestore

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class FirebaseAuthService {

    companion object {
        private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
        fun isUserLoggedIn() : Boolean {
            return auth.currentUser != null
        }

        fun getCurrentUser() : FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

        fun signUp(email: String, password: String, activity: Activity, onResult: (Boolean) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) {
                    if (it.isSuccessful) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                    it.addOnFailureListener {
                        Log.e("Fail", it.printStackTrace().toString())
                    }
                }
        }

        fun signIn(email: String, password: String, activity: Activity, onResult: (Boolean) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) {
                    if (it.isSuccessful) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                    it.addOnFailureListener {
                        Log.e("Fail", it.printStackTrace().toString())
                    }
                }
        }

        fun signOut() {
            auth.signOut()
        }
    }
}