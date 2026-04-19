package com.example.kindred.DataModels

import kotlinx.serialization.Serializable

/**
 * Data class representing a suggested book.
 *
 */
@Serializable
data class BookSuggestion (
    val suggestion_id: Int? = null,
    val user_id: String? = null,
    val entity_type: String? = null,
    val title: String? = null,
    val author: String? = null,
    val reason: String? = null,
    val created_at: String? = null
)
