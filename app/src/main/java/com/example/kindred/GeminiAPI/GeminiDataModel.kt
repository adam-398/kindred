package com.example.kindred.GeminiAPI

import kotlinx.serialization.Serializable

/**
 * Step 1, Data Classes
 * Describe the shape of each API JSON response.
 * Gson deserialises the JSON into these classes automatically.
 * Field names must match the JSON keys exactly.
 */
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>
)
@Serializable
data class Candidate(
    val content: Content
)
@Serializable
data class Content(
    val parts: List<Part>
)
@Serializable
data class Part(
    val text: String
)

