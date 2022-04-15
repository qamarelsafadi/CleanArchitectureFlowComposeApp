package com.example.data.db.books.dao


import androidx.room.*
import com.example.data.db.books.entities.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Query("SELECT * FROM BookEntity WHERE authors LIKE  '%' || :author || '%' or authors LIKE :author ")
    fun getSavedBooks(author: String): List<BookEntity>
}