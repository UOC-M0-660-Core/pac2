package edu.uoc.pac2.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.databinding.ActivityBookListBinding
import edu.uoc.pac2.util.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    private var database: FirebaseFirestore = Firebase.firestore
    private lateinit var adapter: BooksListAdapter

    private lateinit var binding: ActivityBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        // Add books data to Firestore [Use for new project with empty Firestore Database]
        // FirestoreBookData.addBooksDataToFirestoreDatabase()

        // Init AdMob
        initAdMob()
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter {
            val animation = ActivityOptions.makeCustomAnimation(this, R.anim.translate_in_bottom,
                    R.anim.translate_out_bottom).toBundle()
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra(BookDetailFragment.ARG_ITEM_ID, it.uid)
            startActivity(intent, animation)
        }
        recyclerView.adapter = adapter
    }

    // Get Books and Update UI
    private fun getBooks() {
        // First load whatever is stored locally
        loadBooksFromLocalDb()
        // Check if Internet is available
        if ((application as MyApplication).hasInternetConnection()) {
            // Internet connection is available, get remote data
            lifecycleScope.launch {
                try {
                    // Convert listener to suspend function
                    val querySnapshot = database.collection("books").get().await()
                    // Handle result
                    querySnapshot?.let {
                        val books: List<Book> = querySnapshot.documents.mapNotNull {
                            it.toObject(Book::class.java)
                        }
                        Log.i(TAG, "Got #${books.count()} books from Firestore")
                        // Update Local content
                        saveBooksToLocalDatabase(books)
                        // Update UI
                        adapter.submitList(books)
                    }
                } catch (t: Throwable) {
                    Log.w(TAG, "Error retrieving books from Firestore", t)
                }
            }
        }
    }

    // Load Books from Room
    private fun loadBooksFromLocalDb() {
        val bookDao = (application as MyApplication).getBookDao()
        // Run in Background, accessing the local database is a memory-expensive operation
        lifecycleScope.launch {
            // Get Books
            val books = withContext(Dispatchers.IO) {
                bookDao.getAllBooks()
            }
            // Update Adapter on the UI Thread
            withContext(Dispatchers.Main) {
                adapter.submitList(books)
            }
        }
    }

    // Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        val bookDao = (application as MyApplication).getBookDao()
        // Run in Background; accessing the local database is a memory-expensive operation
        lifecycleScope.launch(Dispatchers.IO) {
            books.forEach { bookDao.saveBook(it) }
        }
    }

    private fun initAdMob() {
        MobileAds.initialize(this) {
            Log.i(TAG, "Admob initialize completed with status: $it")
        }
        // Load Ad
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        // Optional: set some listeners
        binding.adView.adListener = object : AdListener() {
            override fun onAdOpened() {
                Log.i(TAG, "Ad opened! $$$$")
            }
        }
    }
}