package com.android.tokobuku.repository

import com.android.tokobuku.dao.BookDao
import com.android.tokobuku.model.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val tireDao: BookDao) {
    val allTires: Flow<List<Book>> = tireDao.getAllTire()
    suspend fun insertTire(tire: Book){
        tireDao.insertTire(tire)
    }

    suspend fun deleteTire(tire: Book){
        tireDao.deleteTire(tire)
    }

    suspend fun updateTire(tire: Book){
        tireDao.updateTire(tire)
    }
}