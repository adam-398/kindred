package com.example.kindred.GeminiAPI

import android.util.Log
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.kindred.BuildConfig
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray

/**
 * Makes a call to the Gemini API.
 * Returns a string containing the response based on the ordered importance of attributes.
 */
suspend fun getGeminiSuggestions(
    entities: String,
    attributes: String,
    genres: String,
    order: List<String>
): String {

    val prompt = "Based on the following items I have enjoyed: $entities. " +
            "Focusing on these attributes: $attributes, and these genres: $genres " +
            "Ranked by importance of: ${order.joinToString(", ")}. " +
            "Suggest 5 similar ones i might enjoy and provide a short description of why each is appropriate." +
            "Return ONLY a JSON array with no markdown, no code blocks, just raw JSON in this exact format: " +
            "[{\"title\": \"\", \"author\": \"\", \"narrator\": \"\", \"reason\": \"\"}]"

    val requestBody = buildJsonObject {
        putJsonArray("contents") {
            addJsonObject {
                putJsonArray("parts") {
                    addJsonObject {
                        put("text", JsonPrimitive(prompt))
                    }
                }
            }
        }
    }.toString()

    Log.d("GeminiTest", "API Key: ${BuildConfig.GEMINI_API_KEY}")

    val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite-preview:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
    Log.d("GeminiTest", "URL: $url")

    val client = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody.toRequestBody("application/json".toMediaType()))
        .build()

    return withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: return@withContext ""

        if (!response.isSuccessful) {
            Log.e("GeminiTest", "Error body: $responseBody")
            throw Exception("Gemini error ${response.code}: try again in a moment")
        }

        val json = Json { ignoreUnknownKeys = true }
        val geminiResponse = json.decodeFromString<GeminiResponse>(responseBody)
        geminiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
    }
}