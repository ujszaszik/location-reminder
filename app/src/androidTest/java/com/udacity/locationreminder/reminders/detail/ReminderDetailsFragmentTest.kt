package com.udacity.locationreminder.reminders.detail

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.matchers.withImageDrawable
import com.udacity.locationreminder.util.*
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinApiExtension
import org.koin.test.KoinTest

@KoinApiExtension
@MediumTest
@RunWith(AndroidJUnit4ClassRunner::class)
@ExperimentalCoroutinesApi
class ReminderDetailsFragmentTest : KoinTest, TestCase() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val dataBindingIdlingResource =
        DataBindingIdlingResource()

    @Before
    fun registerIdlingResources() {
        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
    }

    @After
    fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().apply {
            unregister(EspressoIdlingResource.countingIdlingResource)
            unregister(dataBindingIdlingResource)
        }
    }

    @Test
    fun testReminderDetails_whenReminderIsAttached_thenAppropriateFieldsAreDisplayedInScreen() {
        runBlocking {

            // GIVEN REMINDER IS ATTACHED TO DETAILS
            val testReminder = ReminderEntity.forTesting()
            val testBundle = ReminderDetailsFragmentArgs(testReminder.id.toString()).toBundle()
            val scenario =
                launchFragmentInContainer<ReminderDetailsFragment>(testBundle, R.style.AppTheme)

            // WHEN FRAGMENT IS LOADAD TO SCREEN
            scenario.onFragment { fragment -> fragment.bindForTesting() }

            dataBindingIdlingResource.monitorFragment(scenario)

            val context = ApplicationProvider.getApplicationContext<Context>()

            //THEN CONTENT OF VIEW MATCHES WITH REMINDER DATA
            onView(withId(R.id.ttileLabelTextView)).check(matches(isDisplayed()))
            onView(withId(R.id.ttileLabelTextView))
                .checkHasText(context, R.string.reminder_details_title)

            onView(withId(R.id.titleValueTextView)).check(matches(withText(testReminder.title)))

            onView(withId(R.id.descriptionLabelTextView)).check(matches(isDisplayed()))
            onView(withId(R.id.descriptionLabelTextView))
                .checkHasText(context, R.string.reminder_details_description)

            onView(withId(R.id.descriptionValueTextView)).check(matches(withText(testReminder.description)))

            onView(withId(R.id.locationLabelTextView)).check(matches(isDisplayed()))
            onView(withId(R.id.locationLabelTextView))
                .checkHasText(context, R.string.reminder_details_location)

            onView(withId(R.id.locationValueTextView)).check(matches(withText(testReminder.locationName)))

            onView(withId(R.id.radiusLabelTextView)).check(matches(isDisplayed()))
            onView(withId(R.id.radiusLabelTextView))
                .checkHasText(context, R.string.reminder_details_radius)

            onView(withId(R.id.radiusValueTextView)).check(matches(withText("${testReminder.radius} metres")))

            onView(withId(R.id.lastModifiedLabelTextView)).check(matches(isDisplayed()))
            onView(withId(R.id.lastModifiedLabelTextView))
                .checkHasText(context, R.string.reminder_details_last_modified)
        }
    }

    @Test
    fun testNavigation_whenBackButtonPressed_NavigatesToReminderList() {
        runBlocking {

            // GIVEN WE ARE IN REMINDER DETAILS FRAGMENT
            val navController = TestNavHostController(
                ApplicationProvider.getApplicationContext()
            )
            navController.setGraph(R.navigation.nav_graph)

            val testReminder = ReminderEntity.forTesting()
            val testBundle = ReminderDetailsFragmentArgs(testReminder.id.toString()).toBundle()

            val titleScenario =
                launchFragmentInContainer<ReminderDetailsFragment>(testBundle, R.style.AppTheme)

            dataBindingIdlingResource.monitorFragment(titleScenario)

            titleScenario.onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
                navController.setCurrentDestination(R.id.reminderDetailsFragment)
            }

            // WHEN CLICK ON FLOATING ACTION BUTTON
            onView(withId(R.id.backButton)).perform(click())

            // THEN NAVIGATES BACK TO REMINDER LIST FRAGMENT
            navController.assertCurrentDestinationIs(R.id.reminderListFragment)
        }
    }

    @Test
    fun testReminderDetails_whenFragmentLoaded_BackButtonShowsAppropriately() {
        runBlocking {

            // GIVEN WE LAUNCH TEST REMINDER DETAILS
            val testReminder = ReminderEntity.forTesting()
            val testBundle = ReminderDetailsFragmentArgs(testReminder.id.toString()).toBundle()

            // WHEN SCREEN IS LOADED
            val scenario =
                launchFragmentInContainer<ReminderDetailsFragment>(testBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            // THEN BACK BUTTON IS VISIBLE WITH BACK ARROW DRAWABLE
            onView(withId(R.id.backButton))
                .check(matches(isDisplayed()))
                .check(matches(withImageDrawable(R.drawable.ic_arrow_back_white)))
        }
    }


}