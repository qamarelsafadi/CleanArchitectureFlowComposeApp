package com.example.data.repository

import com.example.domain.common.Resource
import com.example.domain.model.volume.Volume
import kotlinx.coroutines.flow.Flow

interface BooksLocalDataSource {
    suspend fun insert(volume: Volume)
    suspend fun getBooks(author: String): Flow<Resource<List<Volume>>>
}