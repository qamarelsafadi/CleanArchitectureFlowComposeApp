package com.example.data.db.books.dao


import androidx.room.*
import com.example.data.db.books.entities.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Query("SELECT * FROM BookEntity")
    fun getSavedBooks(): List<BookEntity>
}