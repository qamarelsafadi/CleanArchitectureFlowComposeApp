package com.example.domain.usecase

import com.example.domain.repository.BooksRepository

class GetBooksUseCase(private val booksRepository: BooksRepository) {
    suspend operator fun invoke(author: String) = booksRepository.getBooks(author)
}