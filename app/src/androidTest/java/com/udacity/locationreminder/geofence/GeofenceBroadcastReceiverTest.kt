package com.udacity.locationreminder.geofence

import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.location.GeofenceStatusCodes
import com.udacity.locationreminder.MainActivity
import com.udacity.locationreminder.R
import com.udacity.locationreminder.database.fakeDataSourceModule
import com.udacity.locationreminder.matchers.ToastMatcher
import com.udacity.locationreminder.util.CoroutineTestRule
import com.udacity.locationreminder.util.ToastManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest

@ExperimentalCoroutinesApi
@KoinApiExtension
@RunWith(AndroidJUnit4::class)
@MediumTest
class GeofenceBroadcastReceiverTest : KoinTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val gmsErrorIntentKey = "gms_error_code"

    private val broadcastIntent = Intent(context, GeofenceBroadcastReceiver::class.java).also {
        it.action = GeofenceManager.ACTION_GEOFENCE_EVENT
    }

    @Before
    fun init() {
        loadKoinModules(fakeDataSourceModule)
    }

    @Test
    fun testOnReceive_whenGeofenceNotAvailable_AppropriateToastShown() {
        runBlocking {
            activityRule.scenario.onActivity { activity ->
                broadcastIntent.apply {
                    putExtra(gmsErrorIntentKey, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE)
                    activity.sendBroadcast(this)
                }
            }

            ToastManager.cancelAllVisible()

            onView(withText(R.string.geofence_error_not_available))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))

            activityRule.scenario.close()
        }
    }

    @Test
    fun testOnReceive_whenTooManyGeofences_AppropriateToastShown() {
        runBlocking {
            activityRule.scenario.onActivity { activity ->
                broadcastIntent.apply {
                    putExtra(gmsErrorIntentKey, GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES)
                    activity.sendBroadcast(this)
                }
            }

            ToastManager.cancelAllVisible()

            onView(withText(R.string.geofence_error_too_many_geofences))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))

            activityRule.scenario.close()
        }
    }

    @Test
    fun testOnReceive_whenTooManyPendingIntents_AppropriateToastShown() {
        runBlocking {
            activityRule.scenario.onActivity { activity ->
                broadcastIntent.apply {
                    putExtra(
                        gmsErrorIntentKey,
                        GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS
                    )
                    activity.sendBroadcast(this)
                }
            }

            ToastManager.cancelAllVisible()

            onView(withText(R.string.geofence_error_too_many_intents))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
            activityRule.scenario.close()
        }
    }

    @Test
    fun testOnReceive_whenUnknownGeofencingError_AppropriateToastShown() {
        runBlocking {
            activityRule.scenario.onActivity { activity ->
                broadcastIntent.apply {
                    putExtra(gmsErrorIntentKey, 0)
                    activity.sendBroadcast(this)
                }
            }

            ToastManager.cancelAllVisible()

            onView(withText(R.string.geofence_error_unknown_error))
                .inRoot(ToastMatcher())
                .check(matches(isDisplayed()))
            activityRule.scenario.close()
        }
    }


}