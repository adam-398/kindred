package com.example.kindred.DataModels

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Data class representing an item from the Audible JSON file.
 */
@Serializable
data class AudibleItem(
    val asin: String = "", // Added the default value to fix the "must be initialized" error
    val title: String,
    val authors: List<AudibleAuthor>,
    val narrators: List<AudibleNarrator>? = null,
    val blurb: String? = null,
    val length: String? = null,
    val series: List<AudibleSeries>? = null,
    val collectionIds: List<String>? = null,
    val progress: JsonElement? = null
)

@Serializable
data class AudibleAuthor(
    val name: String
)

@Serializable
data class AudibleNarrator(
    val name: String
)

@Serializable
data class AudibleSeries(
    val name: String
)


@Serializable
data class AudibleExport(
    val books: List<AudibleItem>? = null
)