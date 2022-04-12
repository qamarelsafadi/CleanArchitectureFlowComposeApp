package com.example.domain.repository

import com.example.domain.common.Resource
import com.example.domain.model.volume.Volume
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun getBooks(author: String): Flow<Resource<List<Volume>>>
}