package com.example.kindred

import android.content.Context
import android.util.Log
import com.example.kindred.DataModels.AudibleItem
import com.example.kindred.DataModels.Audiobook
import com.example.kindred.DataModels.Book
import com.example.kindred.DataModels.Movie
import com.example.kindred.DataModels.TvShow
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SettingsSessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest


/**
 * Singleton Supabase client object.
 * Must be initialized with a Context before use by calling initialize(context).
 * Manages authentication and session storage via SharedPreferences.
 */

object SupabaseClient {


    lateinit var supabase: io.github.jan.supabase.SupabaseClient
        private set

    fun initialize(context: Context) {
        supabase = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                sessionManager = SettingsSessionManager(
                    SharedPreferencesSettings(
                        context.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)
                    )
                )
            }
        }
    }
}

/**
 * Signs in with email and password.
 * Returns true on success, false on failure.
 * @param email The users email.
 * @param password The user's password.
 */
suspend fun loginUser(email: String, password: String): Boolean {
    return try {
        Log.i("Login", "Attempting sign in...")
        SupabaseClient.supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        Log.i("Login", "Sign in succeeded!")
        true
    } catch (e: Exception) {
        Log.i("Login", "Login failed: ${e.message}")
        Log.i("Login", "Exception type: ${e::class.java.simpleName}")
        e.printStackTrace()
        false
    }
}

/**
 * Logs out the current user.
 */
suspend fun logoutUser() {
    SupabaseClient.supabase.auth.signOut()
}


/**
 * Sends book data to Supabase
 */
suspend fun sendBookData(book: Book) {
    SupabaseClient.supabase.postgrest["books"]
        .insert(book)
}

/**
 * Gets all books from Supabase
 */
suspend fun getBooks(): List<Book> {
    return SupabaseClient.supabase.postgrest["books"]
        .select()
        .decodeList<Book>()
}

/**
 * Deletes book from Supabase
 */
suspend fun deleteBook(bookId: Int) {
    SupabaseClient.supabase.postgrest["books"]
        .delete {
            filter {
                eq("book_id", bookId)
            }
        }
}


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