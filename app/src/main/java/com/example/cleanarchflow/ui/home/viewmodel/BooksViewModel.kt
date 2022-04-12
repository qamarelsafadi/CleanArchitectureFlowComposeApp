package com.example.cleanarchflow.ui.home.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.cleanarchflow.ui.home.mappers.BooksMapper
import com.example.cleanarchflow.ui.home.model.Books
import com.example.cleanarchflow.ui.home.model.Event
import com.example.cleanarchflow.ui.home.model.Resource
import com.example.domain.common.Status
import com.example.domain.usecase.GetBooksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BooksViewModel (
    app: Application,
    private val getBooksUseCase: GetBooksUseCase,
    private val mapper: BooksMapper
) : AndroidViewModel(app) {

    private val _remoteBooks =
        MutableStateFlow<Event<Resource<MutableList<Books>>>>(Event(Resource.loading(null)))
    val remoteBooks: Flow<Event<Resource<MutableList<Books>>>> = _remoteBooks

    // Getting books with uncle bob as default author :)
    fun getBooks(author: String) {

        viewModelScope.launch {
            val booksResult = getBooksUseCase.invoke(author)
            booksResult.collectLatest {
                books ->
                when (books.status) {
                    Status.SUCCESS -> {
                        books.data?.let {
                            var booksList = _remoteBooks.value.peekContent().data
                            booksList?.clear()
                            booksList = mapper.fromVolumeToBook(it).toMutableList()
                            _remoteBooks.value = Event(Resource.success(booksList))
                        }
                    }
                    Status.ERROR -> {
                        _remoteBooks.value = Event(
                            Resource.error(
                                mutableListOf(),
                                books.message,
                                books.errors
                            )
                        )
                    }
                    else -> {
                        _remoteBooks.value = Event(
                            Resource.loading(
                                null
                            )
                        )
                    }
                }

            }
        }
    }
    class BooksViewModelFactory(
        val app: Application,
        private val getBooksUseCase: GetBooksUseCase,
        private val mapper: BooksMapper
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
           return BooksViewModel(
                app,
                getBooksUseCase,
                mapper
            ) as T
        }
    }
}