package com.udacity.locationreminder.util

import android.view.View
import android.view.View.FIND_VIEWS_WITH_TEXT
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.contrib.RecyclerViewActions
import org.hamcrest.Matcher
import org.junit.Assert

fun hasViewWithTextAtPosition(index: Int, text: CharSequence): ViewAssertion? {
    return ViewAssertion { view, e ->
        if (view !is RecyclerView) {
            throw e
        }
        val outViews: ArrayList<View> = ArrayList()
        view.findViewHolderForAdapterPosition(index)!!.itemView.findViewsWithText(
            outViews, text,
            FIND_VIEWS_WITH_TEXT
        )
        Assert.assertTrue(outViews.isNotEmpty())
    }
}

fun hasItemsCount(count: Int): ViewAssertion? {
    return ViewAssertion { view, e ->
        if (view !is RecyclerView) {
            throw e
        }
        Assert.assertTrue(view.adapter!!.itemCount == count)
    }
}

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(
            uiController: UiController,
            view: View
        ) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }
}

fun <T : RecyclerView.ViewHolder> actionOnFirstItem(action: ViewAction): ViewAction {
    return RecyclerViewActions.actionOnItemAtPosition<T>(0, action)
}