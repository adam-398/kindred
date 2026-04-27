package com.example.kindred.Supabase

import com.example.kindred.DataModels.Movie
import com.example.kindred.DataModels.MovieSuggestion
import io.github.jan.supabase.postgrest.postgrest


/**
 * Sends movie data to Supabase
 */
suspend fun sendMovieData(movie: Movie) {
    SupabaseClient.supabase.postgrest["movies"]
        .insert(movie)
}

/**
 * gets all movies from Supabase
 */
suspend fun getMovies(): List<Movie> {
    return SupabaseClient.supabase.postgrest["movies"]
        .select()
        .decodeList<Movie>()
}

/**
 * deletes the movie from Supabase
 */
suspend fun deleteMovie(movieId: Int) {
    SupabaseClient.supabase.postgrest["movies"]
        .delete {
            filter {
                eq("movie_id", movieId)
            }
        }
}



/**
 * Gets movie suggestion from Supabase
 */
suspend fun getMovieSuggestions(): List<MovieSuggestion> {
    return SupabaseClient.supabase.postgrest["suggestions"]
        .select {
            filter { eq("entity_type", "movie") }
        }
        .decodeList<MovieSuggestion>()
}

/**
 * deletes the movie suggestion from Supabase
 */
suspend fun deleteMovieSuggestion(suggestionId: Int) {
    SupabaseClient.supabase.postgrest["suggestions"]
        .delete {
            filter { eq("suggestion_id", suggestionId) }
        }
}
