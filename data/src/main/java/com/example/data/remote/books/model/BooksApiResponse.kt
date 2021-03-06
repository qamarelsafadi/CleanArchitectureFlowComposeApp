package com.example.data.remote.books.model

import com.squareup.moshi.Json


class BooksApiResponse(val items: List<Item>)

data class Item(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "volumeInfo")
    val volumeInfo: ApiVolumeInfo
)

data class ApiVolumeInfo(
    val title: String,
    val imageLinks: ImageLinks?,
    @field:Json(name = "authors")
    var authors: List<String>? = listOf(),
)

data class ImageLinks(val smallThumbnail: String, val thumbnail: String)