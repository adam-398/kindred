package com.example.kindred.DataModels

import kotlinx.serialization.Serializable

/**
 * Data class representing a suggested audiobook.
 *
 */
@Serializable
data class SuggestedAudiobook (
    val suggestion_id: Int? = null,
    val user_id: String? = null,
    val title: String? = null,
    val author: String? = null,
    val narrator: String? = null,
    val reason: String? = null,
)
