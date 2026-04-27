package com.example.kindred.Supabase

import com.example.kindred.DataModels.AudibleItem
import com.example.kindred.DataModels.Audiobook
import com.example.kindred.DataModels.AudiobookSuggestion
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest


/**
 * Sends audiobook data to Supabase
 */
suspend fun sendAudiobookData(audioBook: Audiobook) {
    SupabaseClient.supabase.postgrest["audio_books"]
        .insert(audioBook)
}

/**
 * Gets all audiobooks from Supabase
 */
suspend fun getAudiobooks(): List<Audiobook> {
    return SupabaseClient.supabase.postgrest["audio_books"]
        .select()
        .decodeList<Audiobook>()
}

/**
 * Deletes audiobook from Supabase
 */
suspend fun deleteAudiobook(audiobookId: Int) {
    SupabaseClient.supabase.postgrest["audio_books"]
        .delete {
            filter {
                eq("audiobook_id", audiobookId)
            }
        }
}

/**
 * Sends audiobook import (audible) data to Supabase
 */
suspend fun sendAudiobookImport(items: List<AudibleItem>, status: String) {
    val audiobooks = items.map { item ->
        Audiobook(
            user_id = SupabaseClient.supabase.auth.currentSessionOrNull()?.user?.id,
            title = item.title,
            author = item.authors.firstOrNull()?.name,
            narrator = item.narrators?.firstOrNull()?.name,
            duration = item.length,
            status = status,
            is_favourite = false
        )
    }
    SupabaseClient.supabase.postgrest["audio_books"].insert(audiobooks)
}


/**
 * Gets audiobook suggestion from Supabase
 */
suspend fun getAudiobookSuggestions(): List<AudiobookSuggestion> {
    return SupabaseClient.supabase.postgrest["suggestions"]
        .select {
            filter { eq("entity_type", "audiobook") }
        }
        .decodeList<AudiobookSuggestion>()
}


/**
 * deletes the audiobook suggestion from Supabase
 */
suspend fun deleteAudiobookSuggestion(suggestionId: Int) {
    SupabaseClient.supabase.postgrest["suggestions"]
        .delete {
            filter { eq("suggestion_id", suggestionId) }
        }
}


/**
 * Sends audiobook suggestion to Supabase
 */
suspend fun sendAudiobookSuggestionData(audiobookSuggestion: AudiobookSuggestion) {
    SupabaseClient.supabase.postgrest["suggestions"]
        .insert(audiobookSuggestion)
}