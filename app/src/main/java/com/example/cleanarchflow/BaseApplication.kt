package com.example.cleanarchflow


import android.app.Application
import com.example.cleanarchflow.helper.ServiceLocator
import com.example.cleanarchflow.ui.home.mappers.BooksMapper
import com.example.data.repository.BooksRepositoryImpl
import com.example.domain.usecase.GetBooksUseCase

class BaseApplication : Application() {
    private val booksRepository: BooksRepositoryImpl
        get() = ServiceLocator.provideBooksRepository(this)

    val getBooksUseCase: GetBooksUseCase
        get() = GetBooksUseCase(booksRepository)

    val booksMapper = BooksMapper()

    override fun onCreate() {
        super.onCreate()
    }
}