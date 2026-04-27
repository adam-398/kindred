package com.example.kindred.Supabase

import com.example.kindred.DataModels.TvShow
import com.example.kindred.DataModels.TvShowSuggestion
import io.github.jan.supabase.postgrest.postgrest


/**
 * Sends tv show data to Supabase
 */
suspend fun sendTvShowData(tvShow: TvShow) {
    SupabaseClient.supabase.postgrest["tv_shows"]
        .insert(tvShow)
}

/**
 * gets all tv shows from Supabase
 */
suspend fun getTvShows(): List<TvShow> {
    return SupabaseClient.supabase.postgrest["tv_shows"]
        .select()
        .decodeList<TvShow>()
}

/**
 * deletes the tv show from Supabase
 */
suspend fun deleteTvShow(tvShowId: Int) {
    SupabaseClient.supabase.postgrest["tv_shows"]
        .delete {
            filter {
                eq("tv_show_id", tvShowId)
            }
        }
}

/**
 * Gets tv show suggestion from Supabase
 */
suspend fun getTvShowSuggestions(): List<TvShowSuggestion> {
    return SupabaseClient.supabase.postgrest["suggestions"]
        .select {
            filter { eq("entity_type", "tv_show") }
        }
        .decodeList< TvShowSuggestion>()
}

/**
 * deletes the tv show suggestion from Supabase
 */
suspend fun deleteTvShowSuggestion(suggestionId: Int) {
    SupabaseClient.supabase.postgrest["suggestions"]
        .delete {
            filter { eq("suggestion_id", suggestionId) }
        }
}
