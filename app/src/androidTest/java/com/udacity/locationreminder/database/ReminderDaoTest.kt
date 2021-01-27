package com.udacity.locationreminder.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.RoomDatabase
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jraska.livedata.test
import com.udacity.locationreminder.util.CoroutineTestRule
import com.udacity.locationreminder.util.TestReminders
import com.udacity.locationreminder.util.TestReminders.modifiedTestReminder
import com.udacity.locationreminder.util.assertIfNull
import com.udacity.locationreminder.util.assertValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

@KoinApiExtension
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class ReminderDaoTest : KoinTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()


    private val testDatabaseBuilder: RoomDatabase.Builder<ReminderDatabase> by inject()
    private lateinit var testDatabase: ReminderDatabase
    private lateinit var reminderDao: ReminderDao

    private val listOfTestReminders = TestReminders.getAllTestReminders()

    private val testReminder = TestReminders.first
    private val testReminder2 = TestReminders.second

    @Before
    fun init() {
        loadKoinModules(daoTestModule)
        testDatabase = testDatabaseBuilder.build()
        reminderDao = testDatabase.reminderDao()
    }

    @After
    fun after() {
        testDatabase.close()
        unloadKoinModules(daoTestModule)
    }

    @Test
    fun testGetAllReminders_whenRemindersListAdded_thenReturnThemAll() {
        coroutineTestRule.runBlockingTest {
            listOfTestReminders.forEach {
                reminderDao.insertReminder(it)
            }

            val resultList = reminderDao.getAllReminders().test().value()
            Assert.assertTrue(listOfTestReminders.size == resultList.size)

            resultList.forEach {
                Assert.assertTrue(listOfTestReminders.contains(it))
            }
        }
    }

    @Test
    fun testGetById_whenThereIsNoObject_thenReturnNoValue() {
        coroutineTestRule.runBlockingTest {
            reminderDao.insertReminder(testReminder)

            reminderDao.getById(testReminder2.id.toInt())
                .assertIfNull()
        }
    }

    @Test
    fun testSaveReminder_whenReminderSaved_thenEntityExistsInDatabase() {
        coroutineTestRule.runBlockingTest {
            reminderDao.insertReminder(testReminder)

            reminderDao.getById(testReminder.id.toInt())
                .assertValueEquals(testReminder)
        }
    }

    @Test
    fun testUpdateReminder_whenReminderUpdated_thenEntityReturnedWithUpdatedValues() {
        coroutineTestRule.runBlockingTest {
            reminderDao.insertReminder(testReminder)

            reminderDao.updateReminder(modifiedTestReminder)

            val result = reminderDao.getById(testReminder.id.toInt())
                .test().value()

            Assert.assertTrue(result.locationName == TestReminders.MODIFIED_LOCATION_NAME)
            Assert.assertTrue(result.description == TestReminders.MODIFIED_DESCRIPTION)
            Assert.assertTrue(result.coordinate == TestReminders.MODIFIED_COORDINATE)
            Assert.assertTrue(result.radius == TestReminders.MODIFIED_RADIUS)
        }
    }

    @Test
    fun testUpdateReminder_WhenReminderUpdated_noEntityReturnedWithPreviouslyUpdatedValues() {
        coroutineTestRule.runBlockingTest {
            reminderDao.insertReminder(testReminder)

            reminderDao.updateReminder(modifiedTestReminder)

            val resultList = reminderDao.getAllReminders().test().value()

            resultList.forEach {
                Assert.assertTrue(it != testReminder)
            }
        }
    }

    @Test
    fun testDeleteById_whenReminderDeleted_thenGetByIdReturnsNull() {
        coroutineTestRule.runBlockingTest {
            reminderDao.insertReminder(testReminder)

            reminderDao.deleteReminderById(testReminder.id.toInt())

            reminderDao.getById(testReminder.id.toInt()).assertIfNull()
        }
    }

    @Test
    fun testDeleteById_whenReminderDeleted_thenGetAllRemindersDoNotContainDeletedEntity() {
        coroutineTestRule.runBlockingTest {

            listOfTestReminders.forEach {
                reminderDao.insertReminder(it)
            }

            reminderDao.deleteReminderById(testReminder.id.toInt())

            val reminders = reminderDao.getAllReminders().test().value()

            reminders.forEach {
                Assert.assertTrue(it != testReminder)
            }
        }
    }

    @Test
    fun testDeleteAllReminders_whenRemindersDeleted_thenGetAllRemindersReturnsEmptyList() {
        coroutineTestRule.runBlockingTest {

            listOfTestReminders.forEach {
                reminderDao.insertReminder(it)
            }

            reminderDao.deleteAllReminders()

            val reminder = reminderDao.getAllReminders().test().value()

            Assert.assertTrue(reminder.isNullOrEmpty())
        }
    }

}