package com.udacity.locationreminder.login

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser?>() {

    companion object {
        const val ANONYMOUS_USER = "Anonymous"
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { postValue(it.currentUser) }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}

fun FirebaseUser?.currentUserName() = this?.displayName ?: FirebaseUserLiveData.ANONYMOUS_USER