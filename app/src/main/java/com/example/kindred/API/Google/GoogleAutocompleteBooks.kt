package com.example.kindred.API.Google

/**
 * Data class for the Google books autocomplete API
 */

data class GoogleBooksAutocompleteResponse(
    val items: List<BookItem>
)

data class BookItem(
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val categories: List<String>?
)

