package com.example.contentdownloader

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.contentdownloader.database.Content
import com.example.contentdownloader.repository.ContentRepository
import com.example.contentdownloader.repository.di.RepositoryModule
import com.example.contentdownloader.ui.download.DownloadActivity
import com.example.servicetest.FakeContentRepository
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Unit tests for the implementation of [DownloadActivity]
 */
private const val VALID_URL = "https://bit.ly/30aWVDP"
private const val INVALID_URL = "invalidUrl"
private const val ERROR_TEXT_INVALID_URL = "Please enter a valid url!"

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class DownloadActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<DownloadActivity> =
        ActivityScenarioRule(DownloadActivity::class.java)

    @BindValue
    @JvmField
    val fakeContentRepository: ContentRepository = FakeContentRepository()

    private val content = Content(
        contentUrl = "url",
        fileName = "fileName",
        filePath = "filePath",
        totalLength = 1230L,
        readLength = 123L,
        progress = 10,
        isPaused = false
    )

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_contentListIsNotVisible() {
        onView(withId(R.id.lottieVideoHolder)).check(matches(isDisplayed()))
    }

    @Test
    fun test_AddContent_WithValidURL() {
        onView(withId(R.id.action_add)).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.textInputEditTextUrl)).perform(ViewActions.typeText(VALID_URL))

        onView(withText("ADD")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        Thread.sleep(1000)
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_AddContent_WithInValidURL() {
        onView(withId(R.id.action_add)).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.textInputEditTextUrl)).perform(ViewActions.typeText(INVALID_URL))

        onView(withText("ADD")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.textInputLayoutUrl))
            .check(matches(hasTextInputLayoutErrorText(ERROR_TEXT_INVALID_URL)))
    }

    @Test
    fun test_removeContentFromList() {
        onView(withId(R.id.action_add)).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.textInputEditTextUrl)).perform(ViewActions.typeText(VALID_URL))

        onView(withText("ADD")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0
                    , clickChildViewWithId(R.id.buttonDelete)
                )
            )

        onView(withText("DELETE")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.lottieVideoHolder)).check(matches(isDisplayed()))
    }

    private fun clickChildViewWithId(id: Int): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                TODO("Not yet implemented")
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(id)
                v.performClick()
            }
        }
    }

    private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> =
        object : TypeSafeMatcher<View>() {

            override fun describeTo(description: org.hamcrest.Description?) {}
            override fun matchesSafely(item: View?): Boolean {
                if (item !is TextInputLayout) return false
                val error = item.error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }
        }
}