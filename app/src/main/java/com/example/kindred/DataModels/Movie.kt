package com.example.kindred.DataModels

import kotlinx.serialization.Serializable

/**
 * Data class representing a movie.
 *
 */
@Serializable
data class Movie (
    val movie_id: Int? = null,
    val user_id: String? = null,
    val title: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val starring: String? = null,
    val rating: Int? = null,
    val genres: String? = null,
    val themes: String? = null,
    val notes: String? = null,
    val status: String? = null,
    val is_favourite: Boolean? = null

)