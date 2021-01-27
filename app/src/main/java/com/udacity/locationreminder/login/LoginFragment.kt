package com.udacity.locationreminder.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.locationreminder.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener { UserAuthenticator.loginFrom(this) }

        observeUserState()

        return binding.root
    }

    private fun observeUserState() {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let { navigateToReminderList(it.currentUserName()) }
        })
    }

    private fun navigateToReminderList(username: String) {
        LoginFragmentDirections.actionLoginFragmentToReminderListFragment().run {
            findNavController().navigate(this)
        }
    }

}