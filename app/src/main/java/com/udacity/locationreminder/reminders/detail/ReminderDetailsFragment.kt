package com.udacity.locationreminder.reminders.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.udacity.locationreminder.databinding.FragmentReminderDetailsBinding
import com.udacity.locationreminder.reminders.RemindersSharedViewModel
import com.udacity.locationreminder.util.observeNotNull
import com.udacity.locationreminder.util.wrapEspressoIdlingResource
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class ReminderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentReminderDetailsBinding
    private val args by navArgs<ReminderDetailsFragmentArgs>()
    private val viewModel: RemindersSharedViewModel by viewModel { parametersOf(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderDetailsBinding.inflate(inflater)

        loadReminderByRequestId()
        observeReminderToShow()
        setOnBackButtonPressedListener()

        return binding.root
    }

    private fun loadReminderByRequestId() {
        viewModel.obtainReminderByRequestId(args.requestId)
    }

    private fun observeReminderToShow() {
        viewModel.reminderInDetails.observeNotNull(this) {
            binding.reminder = it
        }
    }

    private fun setOnBackButtonPressedListener() {
        binding.backButton.setOnClickListener {
            ReminderDetailsFragmentDirections.actionReminderDetailsFragmentToReminderListFragment()
                .run { findNavController().navigate(this) }
        }
    }

    @VisibleForTesting
    fun bindForTesting() {
        wrapEspressoIdlingResource {
            viewModel.bindForTesting()
        }
    }
}