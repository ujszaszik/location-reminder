package com.udacity.locationreminder.reminders.list

import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.databinding.FragmentReminderListBinding
import com.udacity.locationreminder.location.LocationPermissionUtil
import com.udacity.locationreminder.login.UserAuthenticator
import com.udacity.locationreminder.reminders.ItemClickListener
import com.udacity.locationreminder.reminders.ReminderType
import com.udacity.locationreminder.reminders.RemindersSharedViewModel
import com.udacity.locationreminder.util.navigate
import com.udacity.locationreminder.util.observeIfNull
import com.udacity.locationreminder.util.observeNotNull
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class ReminderListFragment : Fragment() {

    private lateinit var binding: FragmentReminderListBinding
    private val viewModel: RemindersSharedViewModel by viewModel { parametersOf(requireActivity()) }
    private val args by navArgs<ReminderListFragmentArgs>()

    private val permissionUtil: LocationPermissionUtil by inject { parametersOf(requireActivity()) }
    private var useAuthentication = true

    private lateinit var onEditClickListener: ItemClickListener<ReminderEntity>
    private lateinit var onDeleteClickListener: ItemClickListener<ReminderEntity>

    @RequiresApi(VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderListBinding.inflate(layoutInflater)

        useAuthentication = args.useAuthentication

        setHasOptionsMenu(true)

        handleLocationPermission()

        initListeners()
        setAddReminderButtonListener()

        if (useAuthentication) observeLoggedOutUser()
        observeRemindersList()
        observeEditRequest()

        binding.dataListRecyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    @RequiresApi(VERSION_CODES.Q)
    private fun handleLocationPermission() {
        if (!permissionUtil.isGranted()) {
            permissionUtil.requestPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logoutMenuItem) {
            UserAuthenticator.logoutFrom(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initListeners() {
        onEditClickListener = ItemClickListener { reminder -> viewModel.onEditRequest(reminder) }
        onDeleteClickListener =
            ItemClickListener { reminder -> viewModel.onDeleteRequest(reminder) }
    }

    private fun observeLoggedOutUser() {
        viewModel.currentUser.observeIfNull(this) {
            navigate { ReminderListFragmentDirections.actionReminderListFragmentToLoginFragment() }
        }
    }

    private fun setAddReminderButtonListener() {
        binding.dataListFab.setOnClickListener {
            navigateToReminderManagement(null, ReminderType.CREATE)
        }
    }

    private fun navigateToReminderManagement(reminder: ReminderEntity?, type: ReminderType) {
        navigate {
            ReminderListFragmentDirections
                .actionReminderListFragmentToManageRemindersFragment(reminder, type, null)
        }
    }

    private fun observeRemindersList() {
        viewModel.reminders.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) showEmptyListView()
            else hideEmptyListView()
            showReminders(it)
        })
    }

    private fun hideEmptyListView() {
        binding.noDataLayout.visibility = INVISIBLE
        binding.noDataImage.visibility = INVISIBLE
        binding.noDataLayout.visibility = INVISIBLE
    }

    private fun showReminders(list: List<ReminderEntity>) {
        binding.dataListRecyclerView.adapter =
            ReminderListAdapter(onEditClickListener, onDeleteClickListener).apply {
                submitList(list)
            }
    }

    private fun showEmptyListView() {
        binding.noDataLayout.visibility = VISIBLE
        binding.noDataTextView.visibility = VISIBLE
        binding.noDataImage.visibility = VISIBLE
    }

    private fun observeEditRequest() {
        viewModel.isEditRequested.observeNotNull(this) {
            navigateToReminderManagement(it, ReminderType.UPDATE)
            viewModel.resetEditRequestStatus()
        }
    }

}