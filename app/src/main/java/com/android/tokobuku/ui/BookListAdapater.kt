package com.android.tokobuku.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.tokobuku.R
import com.android.tokobuku.model.Book

class BookListAdapater(
    private val onItemClickListener: (Book) -> Unit
): ListAdapter<Book, BookListAdapater.BookViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return  BookViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
        holder.itemView.setOnClickListener {
            onItemClickListener(book)
        }
    }

    class BookViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val ownerTextView: TextView = itemView.findViewById(R.id.ownerTextView)

        fun bind(book: Book?) {
            nameTextView.text = book?.name
            addressTextView.text = book?.address
            ownerTextView.text = book?.owner
        }

        companion object {
            fun create(parent: ViewGroup): BookListAdapater.BookViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book, parent, false)
                return BookViewHolder(view)
            }
        }
    }
    companion object{
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Book>(){
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}