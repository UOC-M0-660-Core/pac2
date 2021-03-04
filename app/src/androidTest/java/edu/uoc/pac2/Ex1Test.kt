package edu.uoc.pac2

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

/**
 * Created by alex on 04/10/2020.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class Ex1Test {

    private val firestoreDatabase = Firebase.firestore

    @Test
    fun databaseContainsBooks() {
        val signal = CountDownLatch(1)
        firestoreDatabase
                .collection("books")
                .get()
                .addOnSuccessListener {
                    val containsBook = it.documents.mapNotNull { it.data }.any { it[TestData.bookProperty] == TestData.bookPropertyValue }
                    assertTrue(containsBook)
                    signal.countDown()
                }
                .addOnFailureListener {
                    fail("Could not connect to Firestore")
                    signal.countDown()
                }
        signal.await()
    }
}