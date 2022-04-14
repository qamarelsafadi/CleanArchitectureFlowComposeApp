package com.example.data.db.books.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrl: String?
)