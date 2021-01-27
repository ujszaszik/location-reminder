package com.udacity.locationreminder.util

import android.content.Context
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Assert

fun ViewInteraction.checkHasText(context: Context, resId: Int) {
    check(ViewAssertions.matches(ViewMatchers.withText(context.getString(resId))))
}

fun TestNavHostController.assertCurrentDestinationIs(expectedNavigationId: Int) {
    Assert.assertTrue(this.currentDestination?.id == expectedNavigationId)
}