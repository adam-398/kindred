package com.example.kindred

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.kindred.DataModels.AudibleExport
import com.example.kindred.DataModels.AudibleItem
import kotlinx.serialization.json.Json

/**
 * Parses the JSON file and returns a list of AudibleItem objects.
 */
fun parseAudibleJson(context: Context, uri: Uri): List<AudibleItem> {
    val json = Json { ignoreUnknownKeys = true }
    val jsonString = context.contentResolver.openInputStream(uri)
        ?.bufferedReader()
        ?.readText() ?: return emptyList()
    Log.d("Import", "Raw JSON start: ${jsonString.take(500)}")
    return json.decodeFromString<AudibleExport>(jsonString).books ?: emptyList()
}