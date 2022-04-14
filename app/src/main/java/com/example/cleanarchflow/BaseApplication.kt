package com.example.cleanarchflow

import android.app.Application
import com.example.cleanarchflow.ui.home.HomeSL
import com.example.cleanarchflow.ui.home.mappers.BooksMapper
import com.example.data.repository.books.BooksRepositoryImpl
import com.example.domain.features.books.usecase.GetBooksUseCase

class BaseApplication : Application() {
    private val booksRepository: BooksRepositoryImpl
        get() = HomeSL.provideBooksRepository(this)

    val getBooksUseCase: GetBooksUseCase
        get() = GetBooksUseCase(booksRepository)

    val booksMapper = BooksMapper()

    override fun onCreate() {
        super.onCreate()
    }
}