package com.udacity.locationreminder.login

import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI

object UserAuthenticator {

    private const val SIGN_IN_RESULT_CODE = 123

    val currentUser = FirebaseUserLiveData()

    internal fun loginFrom(fragment: Fragment) {
        fragment.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(getProviders())
                .build(), SIGN_IN_RESULT_CODE
        )
    }

    private fun getProviders(): List<AuthUI.IdpConfig> {
        return listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
    }

    internal fun logoutFrom(fragment: Fragment) {
        AuthUI.getInstance().signOut(fragment.requireContext())
    }

}