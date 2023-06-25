package com.android.tokobuku.repository

import com.android.tokobuku.dao.BookDao
import com.android.tokobuku.model.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBook: Flow<List<Book>> = bookDao.getAllBook()
    suspend fun insertBook(book: Book){
        bookDao.insertBook(book)
    }

    suspend fun deleteBook(book: Book){
        bookDao.deleteBook(book)
    }

    suspend fun updateBook(book: Book){
        bookDao.updateBook(book)
    }
}