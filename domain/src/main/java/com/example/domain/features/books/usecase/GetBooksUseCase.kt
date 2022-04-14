package com.example.domain.features.books.usecase

import com.example.domain.features.books.repository.BooksRepository

class GetBooksUseCase(private val booksRepository: BooksRepository) {
    suspend operator fun invoke(author: String) = booksRepository.getBooks(author)
}