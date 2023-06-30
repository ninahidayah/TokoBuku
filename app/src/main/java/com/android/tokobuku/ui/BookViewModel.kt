package com.android.tokobuku.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.tokobuku.model.Book
import com.android.tokobuku.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository): ViewModel(){
    val allBook: LiveData<List<Book>> = repository.allBook.asLiveData()

    fun insert(book: Book) = viewModelScope.launch {
        repository.insertBook(book)
    }

    fun delete(book: Book) = viewModelScope.launch {
        repository.deleteBook(book)
    }

    fun update(book: Book) = viewModelScope.launch {
        repository.updateBook(book)
    }
}

class BookViewModelFactory(private val repository: BookRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((BookViewModel::class.java))) {
            return BookViewModel(repository) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}
