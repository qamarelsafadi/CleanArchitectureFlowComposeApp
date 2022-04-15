package com.example.data.db.books.mappers

import com.example.data.db.books.entities.BookEntity
import com.example.domain.features.books.model.volume.Volume
import com.example.domain.features.books.model.volume.VolumeInfo

class BookEntityMapper {

    fun toBookEntity(volume: Volume): BookEntity {
        return BookEntity(
            id = volume.id,
            title = volume.volumeInfo.title,
            imageUrl = volume.volumeInfo.imageUrl,
            authors = volume.volumeInfo.authors
        )
    }

    fun toVolume(bookEntity: BookEntity): Volume {
        return Volume(
            bookEntity.id,
            VolumeInfo(bookEntity.title,  bookEntity.imageUrl,bookEntity.authors)
        )
    }
}