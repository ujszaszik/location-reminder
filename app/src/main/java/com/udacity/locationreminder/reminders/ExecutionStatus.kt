package com.udacity.locationreminder.reminders

sealed class ExecutionStatus {

    object Success : ExecutionStatus()

    object Error : ExecutionStatus()

    object Loading : ExecutionStatus()
}