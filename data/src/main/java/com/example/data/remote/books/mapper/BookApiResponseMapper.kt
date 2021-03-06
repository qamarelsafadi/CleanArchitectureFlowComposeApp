package com.example.data.remote.books.mapper

import android.util.Log
import com.example.data.remote.books.model.BooksApiResponse
import com.example.domain.features.books.model.volume.Volume
import com.example.domain.features.books.model.volume.VolumeInfo


class BookApiResponseMapper {
    fun toVolumeList(response: BooksApiResponse): List<Volume> {
        return response.items.map {
          if (it.volumeInfo.authors?.isNullOrEmpty() != true) {
              Volume(
                  it.id,
                  VolumeInfo(
                      it.volumeInfo.title,
                      it.volumeInfo.imageLinks?.thumbnail?.replace("http", "https"),
                      it.volumeInfo.authors?.first() ?: ""
                  )
              )
          }else{
              Volume(
                  it.id,
                  VolumeInfo(
                      it.volumeInfo.title,
                      it.volumeInfo.imageLinks?.thumbnail?.replace("http", "https"),
                      ""
                  )
              )
          }
        }
    }
}