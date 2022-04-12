package com.example.data.repository

import com.example.domain.common.Resource
import com.example.domain.model.volume.Volume
import kotlinx.coroutines.flow.Flow

interface BooksRemoteDataSource {
    suspend fun getBooks(author: String): Flow<Resource<List<Volume>>>
}