package edu.uoc.pac2.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book

/**
 * Adapter for a list of Books.
 */

class BooksListAdapter(
        private val onBookClickListener: ((Book) -> Unit),
) : ListAdapter<Book, BooksListAdapter.ViewHolder>(FlowerDiffCallback) {

    private val evenViewType = 0
    private val oddViewType = 1

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            evenViewType
        } else {
            oddViewType
        }
    }

    // Creates View Holder for re-use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = when (viewType) {
            evenViewType -> {
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_book_list_content_even, parent, false)
            }
            oddViewType -> {
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_book_list_content_odd, parent, false)
            }
            else -> {
                throw IllegalStateException("Unsupported viewType $viewType")
            }
        }
        return ViewHolder(view)
    }

    // Binds re-usable View for a given position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = currentList[position]
        holder.titleView.text = book.title
        holder.authorView.text = book.author

        // Set View Click Listener
        holder.view.setOnClickListener { onBookClickListener(book) }
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val authorView: TextView = view.findViewById(R.id.author)
    }


    object FlowerDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }

}
