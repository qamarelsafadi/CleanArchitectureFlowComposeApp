package com.example.domain.features.books.repository

import com.example.domain.common.Resource
import com.example.domain.features.books.model.volume.Volume
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun getBooks(author: String): Flow<Resource<List<Volume>>>
}