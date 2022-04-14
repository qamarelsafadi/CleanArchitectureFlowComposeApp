package com.example.data.repository.books

import android.content.Context
import com.example.data.repository.books.local.BooksLocalDataSource
import com.example.data.repository.books.remote.BooksRemoteDataSource
import com.example.data.util.Common
import com.example.domain.common.Resource
import com.example.domain.features.books.model.volume.Volume
import com.example.domain.features.books.repository.BooksRepository
import kotlinx.coroutines.flow.Flow

class BooksRepositoryImpl(
    private val context: Context,
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource
) : BooksRepository {

    override suspend fun getBooks(author: String): Flow<Resource<List<Volume>>> {
        return if (Common.haveNetworkConnection(context))
            remoteDataSource.getBooks(author)
        else localDataSource.getBooks(author)
    }


}