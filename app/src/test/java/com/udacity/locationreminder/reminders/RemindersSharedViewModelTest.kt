package com.udacity.locationreminder.reminders

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.udacity.locationreminder.database.FakeDataSource
import com.udacity.locationreminder.database.repository.Repository
import com.udacity.locationreminder.database.dataSourceModule
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.geofence.geofenceTestModule
import com.udacity.locationreminder.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@KoinApiExtension
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemindersSharedViewModelTest : KoinTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val viewModel: RemindersSharedViewModel by inject()
    private val reminderRepository: Repository by inject()

    private val testReminder = ReminderEntity.forTesting()

    @Before
    fun init() {
        MockUtil.mockFirebase()
        MockitoAnnotations.initMocks(this)
        startKoin { modules(viewModelTestModule, geofenceTestModule, dataSourceModule) }
    }

    @After
    fun tearDown() {
        val fakeDataSource = reminderRepository.dataSource as FakeDataSource
        fakeDataSource.throwError = false
        unloadKoinModules(listOf(viewModelTestModule, geofenceTestModule, dataSourceModule))
        stopKoin()
    }

    @Test
    fun `when saving reminder execution status updated appropriately`() {
        mainCoroutineRule.pauseDispatcher()

        val type = ReminderType.CREATE
        viewModel.saveReminder(testReminder, type)

        viewModel.executionStatus.testIfEquals(ExecutionStatus.Loading)

        mainCoroutineRule.resumeDispatcher()

        viewModel.executionStatus.testIfEquals(ExecutionStatus.Success)
    }

    @Test
    fun `when error happens during saving reminder execution status updated appropriately`() {
        mainCoroutineRule.pauseDispatcher()

        val fakeDataSource = reminderRepository.dataSource as FakeDataSource
        fakeDataSource.throwError = true

        val type = ReminderType.CREATE
        viewModel.saveReminder(testReminder, type)

        viewModel.executionStatus.testIfEquals(ExecutionStatus.Loading)

        mainCoroutineRule.resumeDispatcher()

        viewModel.executionStatus.testIfEquals(ExecutionStatus.Error)
    }

    @Test
    fun `when saving empty reminder areFieldsEmpty is true`() {
        runBlockingTest {
            val reminder = ReminderEntity.initial()
            val type = ReminderType.CREATE
            viewModel.saveReminder(reminder, type)
        }
        viewModel.areFieldsEmpty.testIfTrue()
    }

    @Test
    fun `when saving valid reminder saveCompleted is true`() {
        runBlockingTest {
            val type = ReminderType.CREATE
            viewModel.saveReminder(testReminder, type)
        }
        viewModel.saveCompleted.testIfTrue()
    }

    @Test
    fun `when edit request triggered live data is updated with that reminder`() {
        viewModel.onEditRequest(testReminder)
        viewModel.isEditRequested.testIfEquals(testReminder)
    }

    @Test
    fun `when map request triggered live data is true`() {
        viewModel.onMapRequest()
        viewModel.isMapRequested.testIfTrue()
    }

    @Test
    fun `when obtain by request id called appropriate reminder is set to live data`() {
        runBlockingTest {
            reminderRepository.createReminder(testReminder)
            val testRequestId = testReminder.requestId
            viewModel.obtainReminderByRequestId(testRequestId)
        }
        viewModel.reminderInDetails.testIfEquals(testReminder)
    }

    @Test
    fun `when reset save completed called live data value is null`() {
        viewModel.resetSaveCompleted()
        viewModel.saveCompleted.testIfNull()
    }

    @Test
    fun `when reset empty field state called live data value is null`() {
        viewModel.resetEmptyFieldState()
        viewModel.areFieldsEmpty.testIfNull()
    }

    @Test
    fun `when reset edit request called live data value is null`() {
        viewModel.resetEditRequestStatus()
        viewModel.isEditRequested.testIfNull()
    }

    @Test
    fun `when reset map request called live data value is null`() {
        viewModel.resetMapRequestStatus()
        viewModel.isMapRequested.testIfNull()
    }

}