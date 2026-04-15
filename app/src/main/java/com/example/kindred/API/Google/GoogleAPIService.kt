package com.example.kindred.API.Google

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Google API service,
 * Defines the contract for the Google.com endpoint.
 * Retrofit generates the implementation at runtime
 * @Query param takes book title
 */

interface GoogleAPIService {
    @GET("books/v1/volumes")
    suspend fun getBooksByTitle(
        @Query("q") title: String
    ): GoogleBooksAutocompleteResponse
}