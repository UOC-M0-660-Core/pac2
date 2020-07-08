package edu.uoc.pac2

import android.app.Application
import androidx.room.Room
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var booksInteractor: BooksInteractor

    override fun onCreate() {
        super.onCreate()
        // TODO: Init Database
        val database = Room.databaseBuilder(applicationContext,
                ApplicationDatabase::class.java, "basedatos-app").build()
        // TODO: Init BooksInteractor
        booksInteractor = BooksInteractor(database.bookDao())
    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    fun hasInternetConnection(): Boolean {
        // TODO: Add Internet Check logic.
        // Check Min and Target SDK Versions for Android API Compatibilities
        return true
    }
}