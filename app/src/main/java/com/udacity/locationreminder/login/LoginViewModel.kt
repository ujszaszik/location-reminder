package com.udacity.locationreminder.login

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val currentUser = UserAuthenticator.currentUser
}