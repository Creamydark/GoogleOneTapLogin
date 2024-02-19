package com.creamydark.googleonetaplogin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class GoogleSignInViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    init {
        viewModelScope.launch {
            auth.addAuthStateListener {
                if (it.currentUser!=null){
                    Log.d("signInWithGoogle", ": ${auth.currentUser?.displayName}")
                }
            }
        }
    }
    fun signInWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Authentication success
                    Log.d("signInWithGoogle", "signInWithGoogle: Successfully Logged in")
                } else {
                    // Authentication failed
                    Log.d("signInWithGoogle", "signInWithGoogle: failed")
                }
            }
    }
}