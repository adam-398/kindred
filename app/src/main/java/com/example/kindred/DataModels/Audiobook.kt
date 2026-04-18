package com.example.kindred.DataModels

import kotlinx.serialization.Serializable

/**
 * Data class representing a book.
 *
 */
@Serializable
data class Audiobook (
    val book_id: Int? = null,
    val user_id: String? = null,
    val title: String? = null,
    val author: String? = null,
    val narrator: String? = null,
    val rating: Int? = null,
    val genres: String? = null,
    val themes: String? = null,
    val notes: String? = null,
    val status: String? = null,
    val is_favourite: Boolean? = null

)