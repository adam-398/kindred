package com.example.kindred.Supabase

import android.content.Context
import android.util.Log
import com.example.kindred.BuildConfig
import com.example.kindred.DataModels.AudibleItem
import com.example.kindred.DataModels.Audiobook
import com.example.kindred.DataModels.AudiobookSuggestion
import com.example.kindred.DataModels.Book
import com.example.kindred.DataModels.BookSuggestion
import com.example.kindred.DataModels.Movie
import com.example.kindred.DataModels.MovieSuggestion
import com.example.kindred.DataModels.TvShow
import com.example.kindred.DataModels.TvShowSuggestion
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
