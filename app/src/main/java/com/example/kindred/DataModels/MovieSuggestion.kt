package com.example.kindred.DataModels

import kotlinx.serialization.Serializable

/**
 * Data class representing a suggested movie.
 *
 */
@Serializable
data class MovieSuggestion (
    val suggestion_id: Int? = null,
    val user_id: String? = null,
    val entity_type: String? = null,
    val title: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val starring: String? = null,
    val reason: String? = null,
    val created_at: String? = null
)
