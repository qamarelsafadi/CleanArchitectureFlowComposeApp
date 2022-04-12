package com.example.data.repository

import android.content.Context
import com.example.data.util.Common
import com.example.domain.common.Resource
import com.example.domain.model.volume.Volume
import com.example.domain.repository.BooksRepository
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