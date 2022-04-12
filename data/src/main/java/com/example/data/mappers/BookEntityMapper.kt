package com.example.data.mappers

import com.example.data.entities.BookEntity
import com.example.domain.model.volume.Volume
import com.example.domain.model.volume.VolumeInfo

class BookEntityMapper {

    fun toBookEntity(volume: Volume): BookEntity {
        return BookEntity(
            id = volume.id,
            title = volume.volumeInfo.title,
            imageUrl = volume.volumeInfo.imageUrl
        )
    }

    fun toVolume(bookEntity: BookEntity): Volume {
        return Volume(
            bookEntity.id,
            VolumeInfo(bookEntity.title,  bookEntity.imageUrl)
        )
    }
}