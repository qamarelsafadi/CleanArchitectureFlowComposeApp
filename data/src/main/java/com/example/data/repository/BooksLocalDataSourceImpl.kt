package com.example.data.repository


import com.example.data.db.BookDao
import com.example.data.mappers.BookEntityMapper
import com.example.domain.common.Resource
import com.example.domain.model.volume.Volume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BooksLocalDataSourceImpl(
    private val bookDao: BookDao,
    private val dispatcher: CoroutineDispatcher,
    private val bookEntityMapper: BookEntityMapper
) : BooksLocalDataSource {

    override suspend fun insert(volume: Volume) = withContext(dispatcher) {
        bookDao.insert(bookEntityMapper.toBookEntity(volume))
    }

    override suspend fun getBooks(author: String): Flow<Resource<List<Volume>>> = withContext(
        Dispatchers.IO
    ) {
        var data: Flow<Resource<List<Volume>>> = MutableStateFlow(Resource.loading(null))
        try {
            val booksFlow = bookDao.getSavedBooks()
            booksFlow.let {
                data = if (it.isNullOrEmpty().not()) {
                    MutableStateFlow(Resource.success(it.map { bookEntity ->
                        bookEntityMapper.toVolume(bookEntity)
                    }, "Data Found"))
                } else {
                    MutableStateFlow(Resource.error(null, "Data Not Found", null))
                }
            }
        } catch (e: Exception) {
            data = MutableStateFlow(Resource.error(null, e.message, null))
        }
        data
    }

}