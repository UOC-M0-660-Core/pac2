package edu.uoc.pac2

import edu.uoc.pac2.data.Book

/**
 * Created by alex on 04/10/2020.
 */
object TestData {
    const val bookProperty = "title"
    const val bookPropertyValue = "The girl on the train"
    const val bookListPosition = 1
    val book = Book(
            uid = 2,
            author = "Paula Hawkins",
            title = "The girl on the train",
    )
    const val networkWaitingMillis = 5000L
    const val uiWaitingMillis = 1000L
}