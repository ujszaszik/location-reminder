package com.udacity.locationreminder.reminders.manage

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.geofence.GeofenceData
import com.udacity.locationreminder.matchers.ToastMatcher
import com.udacity.locationreminder.matchers.hasHintText
import com.udacity.locationreminder.matchers.withImageDrawable
import com.udacity.locationreminder.reminders.ReminderType
import com.udacity.locationreminder.reminders.viewModelAndroidTestModule
import com.udacity.locationreminder.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest


@KoinApiExtension
@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ManageRemindersFragmentTest : KoinTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    companion object {
        private const val TEST_TITLE = "Test Title"
        private const val TEST_DESCRIPTION = "Test Description"
    }

    private val testReminder = ReminderEntity.forTesting()


    private val dataBindingIdlingResource =
        DataBindingIdlingResource()

    @Before
    fun init() {
        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
        loadKoinModules(viewModelAndroidTestModule)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().apply {
            unregister(EspressoIdlingResource.countingIdlingResource)
            unregister(dataBindingIdlingResource)
        }
        unloadKoinModules(viewModelAndroidTestModule)
    }

    @Test
    fun testCreateOrUpdateReminder_whenTitleIsEmpty_thenToastIsShown() {
        val bundle = ManageRemindersFragmentArgs(
            ReminderEntity.initial(),
            ReminderType.CREATE,
            GeofenceData.fromReminder(ReminderEntity.initial())
        ).toBundle()

        // GIVEN MANAGE REMINDERS IS DISPLAYED WITH EMPTY TITLE
        val scenario = launchFragmentInContainer<ManageRemindersFragment>(bundle, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)

        onView(withId(R.id.reminderTitleText)).perform(typeText(TEST_TITLE))

        closeSoftKeyboard()

        //WHEN CLICKING ON SAVE BUTTON
        onView(withId(R.id.saveReminderButton)).perform(click())

        //THEN TOAST APPEARS WITH ALL FIELDS ARE MANDATORY MESSAGE
        onView(withText(R.string.all_fields_are_mandatory)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

    }

    @Test
    fun testCreateOrUpdateReminder_whenDescriptionIsEmpty_thenToastIsShown() {

        // GIVEN MANAGE REMINDERS IS DISPLAYED WITH EMPTY DESCRIPTION
        val bundle = ManageRemindersFragmentArgs(
            ReminderEntity.initial(),
            ReminderType.CREATE,
            GeofenceData.fromReminder(ReminderEntity.initial())
        ).toBundle()

        runBlocking {
            val scenario =
                launchFragmentInContainer<ManageRemindersFragment>(bundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            onView(withId(R.id.reminderDescriptionText)).perform(typeText(TEST_DESCRIPTION))

            closeSoftKeyboard()

            ToastManager.cancelAllVisible()

            //WHEN CLICKING ON SAVE BUTTON
            onView(withId(R.id.saveReminderButton)).perform(click())

            //THEN TOAST APPEARS WITH ALL FIELDS ARE MANDATORY MESSAGE
            onView(withText(R.string.all_fields_are_mandatory)).inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testUpdateReminder_whenReminderBound_thenAppropriateValuesAreShown() {

        //GIVEN EXISTING REMINDER IS LOADED TO UPDATE
        val bundle = ManageRemindersFragmentArgs(
            testReminder,
            ReminderType.UPDATE,
            GeofenceData.fromReminder(testReminder)
        ).toBundle()

        //WHEN SCREEN IS DISPLAYED
        val scenario = launchFragmentInContainer<ManageRemindersFragment>(bundle, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)

        //THEN APPROPRIATE VALUES ARE PRESENT
        onView(withId(R.id.reminderTitleText)).check(matches(withText(testReminder.title)))
        onView(withId(R.id.reminderTitleLayout))
            .check(matches(hasHintText(context.getString(R.string.reminder_management_title))))

        onView(withId(R.id.reminderDescriptionText)).check(matches(withText(testReminder.description)))
        onView(withId(R.id.reminderDescriptionLayout))
            .check(matches(hasHintText(context.getString(R.string.reminder_management_description))))
    }

    @Test
    fun testCreateReminder_whenScreenLoaded_thenFieldsAreEmpty() {

        //GIVEN INITIAL REMINDER STATE
        val bundle = ManageRemindersFragmentArgs(
            ReminderEntity.initial(),
            ReminderType.UPDATE,
            GeofenceData.fromReminder(ReminderEntity.initial())
        ).toBundle()

        //WHEN SCREEN IS DISPLAYED
        val scenario = launchFragmentInContainer<ManageRemindersFragment>(bundle, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)

        //THEN FIELDS ARE EMPTY
        onView(withId(R.id.reminderTitleText)).check(matches(withText("")))
        onView(withId(R.id.reminderTitleLayout)).check(matches(hasHintText(context.getString(R.string.reminder_management_title))))

        onView(withId(R.id.reminderDescriptionText)).check(matches(withText("")))
        onView(withId(R.id.reminderDescriptionLayout))
            .check(matches(hasHintText(context.getString(R.string.reminder_management_description))))
    }


    @Test
    fun testCreateOrUpdateReminder_whenScreenLoaded_thenLocationImageIconIsShown() {

        //GIVEN REMINDER IS ANY STATE
        val bundle = ManageRemindersFragmentArgs(
            ReminderEntity.initial(),
            ReminderType.UPDATE,
            GeofenceData.fromReminder(ReminderEntity.initial())
        ).toBundle()

        //WHEN SCREEN IS LOADED
        val scenario = launchFragmentInContainer<ManageRemindersFragment>(bundle, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)

        //THEN LOCATION ICON IS SHOWN APPROPRIATELY
        onView(withId(R.id.locationImage)).check(matches(isDisplayed()))
            .check(matches(withImageDrawable(R.drawable.ic_location_grey)))
    }


}