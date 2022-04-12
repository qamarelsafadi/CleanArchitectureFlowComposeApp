package com.example.cleanarchflow.helper
import android.annotation.SuppressLint
import android.content.Context
import com.example.cleanarchflow.BuildConfig
import com.example.data.api.NetworkModule
import com.example.data.db.AppDatabase
import com.example.data.mappers.BookApiResponseMapper
import com.example.data.mappers.BookEntityMapper
import com.example.data.repository.BooksLocalDataSource
import com.example.data.repository.BooksLocalDataSourceImpl
import com.example.data.repository.BooksRemoteDataSourceImpl
import com.example.data.repository.BooksRepositoryImpl
import kotlinx.coroutines.Dispatchers

object ServiceLocator {

    private var database: AppDatabase? = null

    private val networkModule by lazy {
        NetworkModule()
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