package com.example.data.db


import androidx.room.*
import com.example.data.entities.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Query("SELECT * FROM BookEntity")
    fun getSavedBooks(): List<BookEntity>
}