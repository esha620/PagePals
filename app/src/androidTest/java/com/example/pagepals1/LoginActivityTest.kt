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

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @Test
    fun testClickToRegisterLaunches() {
        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.registerNow)).perform(click())
        onView(withId(R.id.regMain)).check(matches(isDisplayed()))
    }
}