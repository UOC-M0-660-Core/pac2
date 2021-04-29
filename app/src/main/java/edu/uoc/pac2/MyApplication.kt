package edu.uoc.pac2

import android.app.Application
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var bookDao: BookDao

    override fun onCreate() {
        super.onCreate()
        // TODO: Init Room Database
        // TODO: Init BookDao
    }

    fun getBookDao(): BookDao {
        return bookDao
    }

    fun hasInternetConnection(): Boolean {
        // TODO: Add Internet Check logic.
        return true
    }
}