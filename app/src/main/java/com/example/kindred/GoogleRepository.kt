package com.example.kindred

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * API Reposity,
 * Builds the HTTP client and exposes the suspend function for the ViewModel to call.
 */

class GoogleRepository {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .build()
            chain.proceed(request)
        }
        .build()

    /**
     * Retrofit instance for the Google API.
     * GsonConverterFactory deserialises the JSON response into data classes.
     */

    private val api = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleAPIService::class.java)

    suspend fun getBookData(title: String): List<BookItem> {
        return api.getBooksByTitle(title).items
    }
}


