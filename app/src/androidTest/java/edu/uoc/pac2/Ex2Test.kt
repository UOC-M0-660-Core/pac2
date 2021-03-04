package edu.uoc.pac2

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac2.ui.BookListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 04/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex2Test {

    @Test
    fun listContainsBook() {
        // Start Activity
        val scenario = ActivityScenario.launch(BookListActivity::class.java)
        Thread.sleep(TestData.networkWaitingMillis)
        onView(withText(TestData.book.title)).check(matches(isDisplayed()))
        scenario.close()
    }
}