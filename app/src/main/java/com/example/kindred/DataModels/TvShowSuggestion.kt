package com.example.kindred.DataModels

import kotlinx.serialization.Serializable

/**
 * Data class representing a suggested tv show suggestion.
 *
 */
@Serializable
data class TvShowSuggestion (
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
