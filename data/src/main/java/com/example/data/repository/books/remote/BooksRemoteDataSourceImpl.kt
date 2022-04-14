package com.example.data.repository.books.remote

import com.example.data.remote.books.service.BooksApi
import com.example.data.remote.books.mapper.BookApiResponseMapper
import com.example.data.repository.books.local.BooksLocalDataSourceImpl
import com.example.domain.common.Resource
import com.example.domain.features.books.model.volume.Volume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class BooksRemoteDataSourceImpl(
    private val service: BooksApi,
    private var booksLocalDataSourceImpl: BooksLocalDataSourceImpl,
    private val mapper: BookApiResponseMapper
) : BooksRemoteDataSource {
    override suspend fun getBooks(author: String): Flow<Resource<List<Volume>>> =
        withContext(Dispatchers.IO) {
            val data: Flow<Resource<List<Volume>>> = try {
                val response = service.getBooks(author)
                if (response.isSuccessful) {
                    mapper.toVolumeList(response.body()!!).map {
                        booksLocalDataSourceImpl.insert(it)
                    }
                    MutableStateFlow(Resource.success(mapper.toVolumeList(response.body()!!)))
                } else {
                    MutableStateFlow(Resource.error(null, response.message(), null))
                }
            } catch (e: Exception) {
                MutableStateFlow(Resource.error(null, e.message, null))
            }
            data
        }
}