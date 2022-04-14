package com.example.cleanarchflow.ui.home
import android.annotation.SuppressLint
import android.content.Context
import com.example.cleanarchflow.BuildConfig
import com.example.data.remote.base.RetrofitClient
import com.example.data.db.AppDatabase
import com.example.data.remote.books.mapper.BookApiResponseMapper
import com.example.data.db.books.mappers.BookEntityMapper
import com.example.data.repository.books.local.BooksLocalDataSource
import com.example.data.repository.books.local.BooksLocalDataSourceImpl
import com.example.data.repository.books.remote.BooksRemoteDataSourceImpl
import com.example.data.repository.books.BooksRepositoryImpl
import kotlinx.coroutines.Dispatchers

object HomeSL {

    private var database: AppDatabase? = null

    private val networkModule by lazy {
        RetrofitClient()
    }
    private val bookEntityMapper by lazy {
        BookEntityMapper()
    }

    @SuppressLint("StaticFieldLeak")
    @Volatile
    var booksRepository: BooksRepositoryImpl? = null

    fun provideBooksRepository(context: Context): BooksRepositoryImpl {
        synchronized(this) {
            return booksRepository ?: createBooksRepository(context)
        }
    }

    private fun createBooksRepository(context: Context): BooksRepositoryImpl {
        val database = database ?: createDataBase(context)
        val newRepo = BooksRepositoryImpl(
                context,
                createBooksLocalDataSource(context),
                BooksRemoteDataSourceImpl(
                    networkModule.createBooksApi(BuildConfig.GOOGLE_APIS_ENDPOINT),
                    BooksLocalDataSourceImpl(
                        database.bookDao(),
                        Dispatchers.IO,
                        bookEntityMapper
                    ),
                    BookApiResponseMapper()
                )
            )
        booksRepository = newRepo
        return newRepo
    }

    private fun createBooksLocalDataSource(context: Context): BooksLocalDataSource {
        val database = database ?: createDataBase(context)
        return BooksLocalDataSourceImpl(
            database.bookDao(),
            Dispatchers.IO,
            bookEntityMapper
        )
    }

    private fun createDataBase(context: Context): AppDatabase {
        val result = AppDatabase.getInstance(context)
        database = result
        return result
    }
}