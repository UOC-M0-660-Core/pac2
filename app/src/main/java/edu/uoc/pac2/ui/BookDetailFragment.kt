package edu.uoc.pac2.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.databinding.ActivityBookListBinding
import edu.uoc.pac2.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentBookDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get Book for this detail screen
        loadBook()
    }


    // Get Book for the given {@param ARG_ITEM_ID} Book id
    private fun loadBook() {
        // Get Books DAO
        val bookDao = (requireActivity().application as MyApplication).getBookDao()
        // Here we force cast arguments, which could be null
        // This screen should never be opened without a detail itemId in the arguments.
        // If it's null the app will crash
        val id = arguments!!.getInt(ARG_ITEM_ID)
        // Get book
        viewLifecycleOwner.lifecycleScope.launch {
            val book: Book? = withContext(Dispatchers.IO) {
                bookDao.getBookById(id)
            }
            withContext(Dispatchers.Main) {
                book?.let {
                    initUI(book)
                } ?: run {
                    // Book not found, alert user and finish
                    Toast.makeText(requireContext(), R.string.book_not_found, Toast.LENGTH_LONG).show()
                    requireActivity().finish()
                }
            }
        }
    }

    // Init UI with book details
    private fun initUI(book: Book) {
        binding.bookAuthor.text = book.author
        binding.bookDate.text = book.publicationDate
        binding.bookDetail.text = book.description
        Picasso.get().load(book.urlImage).into(binding.bookImage)
        // Init AppBar
        Picasso.get().load(book.urlImage).into(binding.imageHeader)
        binding.toolbarLayout.title = book.title
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.detailToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Init Share FAB
        binding.fabShare.setOnClickListener {
            shareContent(book)
        }
    }

    // Share Book Title and Image URL
    private fun shareContent(book: Book) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, book.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${book.title}\n${book.urlImage}")
        shareIntent.type = "text/*"
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_info)))
    }

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}