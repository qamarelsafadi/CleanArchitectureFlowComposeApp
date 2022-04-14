package com.example.cleanarchflow.ui.home.mappers

import com.example.cleanarchflow.ui.home.model.Books
import com.example.domain.features.books.model.volume.Volume


class BooksMapper {
    fun fromVolumeToBook(
        volumes: List<Volume>,
    ): List<Books> {
        val books = arrayListOf<Books>()
        for (volume in volumes) {
                books.add(
                    Books(
                        volume.id,
                        volume.volumeInfo.title,
                        volume.volumeInfo.imageUrl)
                )
        }
        return books.sortedBy { it.id }
    }
}