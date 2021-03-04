package edu.uoc.pac2

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac2.ui.BookListActivity
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.Matchers.endsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 04/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex7Test {

    @Test
    fun containsAdBanner() {
        val scenario = ActivityScenario.launch(BookListActivity::class.java)
        Thread.sleep(TestData.networkWaitingMillis)
        onView(withClassName(endsWith("AdView"))).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        scenario.close()
    }
}