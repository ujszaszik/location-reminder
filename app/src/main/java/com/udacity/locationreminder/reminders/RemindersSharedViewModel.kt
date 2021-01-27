package com.udacity.locationreminder.reminders

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.database.repository.Repository
import com.udacity.locationreminder.geofence.IGeofenceManager
import com.udacity.locationreminder.login.UserAuthenticator
import com.udacity.locationreminder.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
open class RemindersSharedViewModel(
    private val reminderRepository: Repository,
    private val geofenceManager: IGeofenceManager
) : ViewModel() {

    val currentUser = UserAuthenticator.currentUser

    val reminders = reminderRepository.remindersList

    private val _reminderInDetails = MutableLiveData<ReminderEntity>()
    val reminderInDetails: LiveData<ReminderEntity> get() = _reminderInDetails

    private val _saveCompleted = MutableLiveData<Boolean>()
    val saveCompleted: LiveData<Boolean> get() = _saveCompleted

    private val _areFieldsEmpty = MutableLiveData<Boolean>()
    val areFieldsEmpty: LiveData<Boolean> get() = _areFieldsEmpty

    private val _isEditRequested = MutableLiveData<ReminderEntity>()
    val isEditRequested: LiveData<ReminderEntity> get() = _isEditRequested

    private val _isMapRequested = MutableLiveData<Boolean>()
    val isMapRequested: LiveData<Boolean> get() = _isMapRequested

    private val _executionStatus = MutableLiveData<ExecutionStatus>()
    val executionStatus: LiveData<ExecutionStatus> get() = _executionStatus

    fun saveReminder(reminderEntity: ReminderEntity, type: ReminderType) {
        wrapEspressoIdlingResource {
            _executionStatus.postValue(ExecutionStatus.Loading)
            viewModelScope.launch {
                try {
                    if (reminderEntity.hasEmptyFields()) {
                        _areFieldsEmpty.postValue(true)
                    } else {
                        handleSaveRequest(reminderEntity, type)
                        handleGeofence(reminderEntity, type)
                        _saveCompleted.postValue(true)
                    }
                    _executionStatus.postValue(ExecutionStatus.Success)
                } catch (e: Exception) {
                    _executionStatus.postValue(ExecutionStatus.Error)
                }
            }
        }
    }

    private suspend fun handleSaveRequest(reminderEntity: ReminderEntity, type: ReminderType) {
        when (type) {
            ReminderType.CREATE -> reminderRepository.createReminder(reminderEntity)
            ReminderType.UPDATE -> reminderRepository.updateReminder(reminderEntity)
        }
    }

    private fun handleGeofence(reminderEntity: ReminderEntity, type: ReminderType) {
        when (type) {
            ReminderType.CREATE -> geofenceManager.createGeofence(reminderEntity, false)
            ReminderType.UPDATE -> geofenceManager.updateGeofence(reminderEntity)
        }
    }


    fun onEditRequest(reminderEntity: ReminderEntity) {
        _isEditRequested.postValue(reminderEntity)
    }

    fun onMapRequest() {
        _isMapRequested.postValue(true)
    }

    fun onDeleteRequest(reminder: ReminderEntity) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
            geofenceManager.removeGeofence(reminder.requestId, false)
        }
    }

    fun obtainReminderByRequestId(requestId: String) {
        reminders.value?.forEach {
            if (requestId == it.requestId) postCurrentReminder(it)
        }
    }

    private fun postCurrentReminder(reminderEntity: ReminderEntity) {
        _reminderInDetails.postValue(reminderEntity)
    }

    fun resetSaveCompleted() {
        _saveCompleted.value = null
    }

    fun resetEmptyFieldState() {
        _areFieldsEmpty.value = null
    }

    fun resetEditRequestStatus() {
        _isEditRequested.value = null
    }

    fun resetMapRequestStatus() {
        _isMapRequested.value = null
    }

    @VisibleForTesting
    fun bindForTesting() {
        wrapEspressoIdlingResource {
            _reminderInDetails.postValue(ReminderEntity.forTesting())
        }
    }

}