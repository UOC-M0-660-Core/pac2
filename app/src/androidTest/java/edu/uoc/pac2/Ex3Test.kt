package edu.uoc.pac2

import android.os.AsyncTask
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.ui.BookListActivity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


/**
 * Created by alex on 04/10/2020.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex3Test {

    @Test
    fun listContainsBook() {
        val scenario = ActivityScenario.launch(BookListActivity::class.java)
        Thread.sleep(TestData.networkWaitingMillis)
        onView(withText(TestData.book.title)).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun booksInteractorReturnsBook() {
        val scenario = ActivityScenario.launch(BookListActivity::class.java)
        Thread.sleep(TestData.networkWaitingMillis)
        scenario.onActivity {
            runBlocking {
                val interactor = (it.application as MyApplication).getBooksInteractor()
                val signal = CountDownLatch(1)
                var localBooks = emptyList<Book>()
                AsyncTask.execute {
                    localBooks = interactor.getAllBooks()
                    signal.countDown()
                }
                signal.await()
                assertTrue(localBooks.map { it.title }.contains(TestData.book.title))
            }
        }
        scenario.close()
    }
}