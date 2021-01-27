package com.udacity.locationreminder.reminders.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jraska.livedata.test
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.repository.Repository
import com.udacity.locationreminder.database.entity.ReminderEntity
import com.udacity.locationreminder.database.fakeDataSourceModule
import com.udacity.locationreminder.matchers.ToastMatcher
import com.udacity.locationreminder.matchers.withImageDrawable
import com.udacity.locationreminder.util.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

@KoinApiExtension
class ReminderListFragmentTest : KoinTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val dataBindingIdlingResource =
        DataBindingIdlingResource()

    private val testReminder = ReminderEntity.forTesting()

    private val modifiedTestReminder = testReminder.apply {
        locationName = MODIFIED_LOCATION_NAME
        title = MODIFIED_TITLE
    }

    private val noAuthBundle = ReminderListFragmentArgs(false).toBundle()

    private val fakeRepository: Repository by inject()

    companion object {
        private const val MODIFIED_LOCATION_NAME = "modifiedLocationName"
        private const val MODIFIED_TITLE = "modifiedTitle"
    }

    @Before
    fun init() {
        loadKoinModules(fakeDataSourceModule)
        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
    }

    @After
    fun tearDown() {
        unloadKoinModules(fakeDataSourceModule)
        IdlingRegistry.getInstance().apply {
            unregister(EspressoIdlingResource.countingIdlingResource)
            unregister(dataBindingIdlingResource)
        }
    }

    @Test
    fun testReminderListFragment_whenThereAreNoItems_thenNoDataImageAndTextsAreVisible() {
        runBlocking {
            // GIVEN WE LAUNCH REMINDER LIST FRAGMENT

            // WHEN THERE ARE NO ITEMS IN REMINDERS LIST
            fakeRepository.remindersList.test()

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)


            closeSoftKeyboard()

            // THEN NO DATA LAYOUT WITH IMAGE AND TEXT VIEW ARE VISIBLE
            onView(withId(R.id.noDataLayout)).check(matches(isDisplayed()))
            onView(withId(R.id.noDataImage)).check(matches(isDisplayed()))
                .check(matches(withImageDrawable(R.drawable.ic_no_data)))
            onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
                .check(matches(withText(R.string.no_data)))
        }
    }

    @Test
    fun testReminderListFragment_whenThereIsItem_thenItemDetailsAreDisplayed() {
        runBlocking {

            // WHEN THERE IS AN ITEM IN REMINDERS LIST
            val testReminder = ReminderEntity.forTesting()
            fakeRepository.createReminder(testReminder)
            val reminderList = fakeRepository.remindersList.test().value()

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            //THEN NO DATA LAYOUT IS NOT VISIBLE
            onView(withId(R.id.noDataLayout)).check(matches(not(isDisplayed())))
            onView(withId(R.id.noDataImage)).check(matches(not(isDisplayed())))
            onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))

            //THEN RECYCLER VIEW IS VISIBLE WITH ACCURATE ITEM COUNT AND VALUES DISPLAYED
            onView(withId(R.id.dataListRecyclerView))
                .check(matches(isDisplayed()))
                .check(hasItemsCount(reminderList.size))
                .check(
                    hasViewWithTextAtPosition(0, testReminder.title)
                )
                .check(
                    hasViewWithTextAtPosition(0, testReminder.locationName)
                )

            onView(withId(R.id.dataListRecyclerView))
                .perform(scrollTo<ReminderListViewHolder>(hasDescendant(withImageDrawable(R.drawable.ic_edit_blue))))
                .perform(scrollTo<ReminderListViewHolder>(hasDescendant(withImageDrawable(R.drawable.ic_delete_red))))

        }
    }

    @Test
    fun testReminderListFragment_whenItemIsUpdated_thenUpdatedValuesAreShown() {
        runBlocking {

            //WHEN A REMINDER IS UPDATED
            fakeRepository.createReminder(ReminderEntity.forTesting())
            fakeRepository.updateReminder(modifiedTestReminder)
            fakeRepository.remindersList.test().value()

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            //THEN UPDATED VALUES ARE SHOWN IN RECYCLER VIEW
            onView(withId(R.id.dataListRecyclerView))
                .check(
                    hasViewWithTextAtPosition(0, modifiedTestReminder.title)
                )
                .check(
                    hasViewWithTextAtPosition(0, modifiedTestReminder.locationName)
                )
        }
    }

    @Test
    fun testReminderListFragment_whenItemDeleteClicked_thenRemoveToastShown() {
        runBlocking {

            fakeRepository.createReminder(ReminderEntity.forTesting())
            fakeRepository.remindersList.test().value()

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)


            //WHEN CLICK ON ICON TO DELETE REMINDER
            onView(withId(R.id.dataListRecyclerView))
                .perform(
                    actionOnItemAtPosition<ReminderListViewHolder>(
                        0, clickChildViewWithId(R.id.deleteIcon)
                    )
                )

            //THEN TOAST IS SHOWN WITH GEOFENCE REMOVAL SUCCESSFUL
            onView(withText(R.string.geofence_remove_successful)).inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testReminderListFragment_whenItemDeleteClicked_itemRecyclerViewIsUpdated() {
        runBlocking {

            //GIVEN WE LAUNCH REMINDER LIST FRAGMENT WITH A REMINDER

            fakeRepository.createReminder(ReminderEntity.forTesting())
            val reminderList = fakeRepository.remindersList.test().value()

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)


            //WHEN CLICK ON ICON TO DELETE REMINDER
            onView(withId(R.id.dataListRecyclerView)).perform(
                actionOnFirstItem<ReminderListViewHolder>(
                    clickChildViewWithId(R.id.deleteIcon)
                )
            )

            //THEN RECYCLER VIEW IS UPDATED
            onView(withId(R.id.dataListRecyclerView))
                .check(hasItemsCount(reminderList.size))
        }
    }

    @Test
    fun testReminderListFragment_whenItemEditClicked_thenNavigatesToReminderManagementAndAppropriateValuesAreShown() {
        runBlocking {

            //GIVEN WE LAUNCH REMINDER LIST FRAGMENT

            fakeRepository.createReminder(ReminderEntity.forTesting())
            fakeRepository.remindersList.test().value()

            val navController =
                TestNavHostController(ApplicationProvider.getApplicationContext())
            navController.setGraph(R.navigation.nav_graph)

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            scenario.onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
                navController.setCurrentDestination(R.id.reminderListFragment)
            }

            //WHEN CLICKING ON EDIT ITEM ICON
            onView(withId(R.id.dataListRecyclerView)).perform(
                actionOnFirstItem<ReminderListViewHolder>(
                    clickChildViewWithId(R.id.editIcon)
                )
            )

            //THEN NAVIGATION HAPPENS TO MANAGE REMINDERS FRAGMENT
            Assert.assertTrue(navController.currentDestination?.id == R.id.manageRemindersFragment)
        }
    }

    @Test
    fun testReminderFragment_whenScreenLoaded_thenAddNewReminderButtonIsShownAppropriately() {
        runBlocking {

            //GIVEN LAUNCHING REMINDER LIST FRAGMENT
            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            //WHEN SCREEN IS LOADED

            //THEN ADD NEW ITEM BUTTON IS VISIBLE
            onView(withId(R.id.dataListFab)).check(matches(isDisplayed()))
                .check(matches(withImageDrawable(R.drawable.ic_add_white)))
        }
    }

    @Test
    fun testReminderFragment_whenAddNewReminderButtonClicked_thenNavigatesToReminderManagementScreen() {
        runBlocking {

            // GIVEN REMINDER DETAILS FRAGMENT IS LOADED
            val navController = TestNavHostController(
                ApplicationProvider.getApplicationContext()
            )
            navController.setGraph(R.navigation.nav_graph)

            val scenario =
                launchFragmentInContainer<ReminderListFragment>(noAuthBundle, R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(scenario)

            scenario.onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController)
                navController.setCurrentDestination(R.id.reminderListFragment)
            }

            closeSoftKeyboard()

            // WHEN CLICK ON FLOATING ACTION BUTTON
            onView(withId(R.id.dataListFab)).perform(ViewActions.click())

            // THEN NAVIGATES BACK TO REMINDER LIST FRAGMENT
            navController.assertCurrentDestinationIs(R.id.manageRemindersFragment)
        }
    }

}