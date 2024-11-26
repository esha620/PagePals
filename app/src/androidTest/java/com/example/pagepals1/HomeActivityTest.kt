package com.example.pagepals1

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.pagepals1.activities.HomeScreen

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @Test
    fun testClickToLogoutLaunches() {
        ActivityScenario.launch(HomeScreen::class.java)
        onView(withId(R.id.logout)).perform(click())
        onView(withId(R.id.loginMain)).check(matches(isDisplayed()))
    }
}