package com.udacity.locationreminder.matchers

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun withImageDrawable(resourceId: Int): Matcher<View?>? {
    return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has image drawable resource $resourceId")
        }

        override fun matchesSafely(imageView: ImageView): Boolean {
            val drawable: Drawable? = imageView.drawable
            val otherDrawable: Drawable = imageView.context.resources.getDrawable(resourceId)
            return drawable?.toBitmap()?.sameAs(otherDrawable.toBitmap()) ?: false
        }
    }
}