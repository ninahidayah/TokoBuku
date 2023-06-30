package com.android.tokobuku.application

import android.app.Application
import com.android.tokobuku.repository.BookRepository

class BookApp: Application(){
    val database by lazy { BookDatabase.getDatabase(this    ) }
    val repository by lazy { BookRepository(database.bookDao()) }
}