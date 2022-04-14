package com.example.data.remote.books.service


import com.example.data.remote.books.model.BooksApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface BooksApi {
    @GET("books/v1/volumes")
    suspend fun getBooks(@Query("q") author: String): Response<BooksApiResponse>
}