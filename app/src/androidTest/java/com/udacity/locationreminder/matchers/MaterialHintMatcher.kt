package com.udacity.locationreminder.matchers

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasHintText(expectedHint: String): Matcher<View> = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {}

    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextInputLayout) return false
        val hint = item.hint ?: return false
        val hintText = hint.toString()
        return expectedHint == hintText
    }
}