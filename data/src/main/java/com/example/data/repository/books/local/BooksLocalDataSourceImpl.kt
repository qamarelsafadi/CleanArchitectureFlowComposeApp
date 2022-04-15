package com.example.data.repository.books.local


import com.example.data.db.books.dao.BookDao
import com.example.data.db.books.mappers.BookEntityMapper
import com.example.domain.common.Resource
import com.example.domain.features.books.model.volume.Volume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class BooksLocalDataSourceImpl(
    private val bookDao: BookDao,
    private val dispatcher: CoroutineDispatcher,
    private val bookEntityMapper: BookEntityMapper
) : BooksLocalDataSource {

    override suspend fun insert(volume: Volume) = withContext(dispatcher) {
        bookDao.insert(bookEntityMapper.toBookEntity(volume))
    }

    override suspend fun getBooks(author: String): List<Volume> = withContext(
        dispatcher
    ) {
        bookDao.getSavedBooks(author).let {
            it.map { bookEntity ->
                bookEntityMapper.toVolume(bookEntity)
            }
        }
    }

}