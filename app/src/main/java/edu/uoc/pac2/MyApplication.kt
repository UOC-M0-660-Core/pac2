package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.Room
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var bookDao: BookDao

    override fun onCreate() {
        super.onCreate()
        // Init Database
        val database = Room.databaseBuilder(applicationContext,
                ApplicationDatabase::class.java, "app_database").build()
        // Init Book Dao
        bookDao = database.bookDao()
    }

    fun getBookDao(): BookDao {
        return bookDao
    }

    fun hasInternetConnection(): Boolean {
        // Check Internet Check connection
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.getNetworkCapabilities(cm.activeNetwork)?.run {
            return@run when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return true
    }
}