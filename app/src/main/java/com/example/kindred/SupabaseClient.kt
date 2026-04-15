package com.example.kindred

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SettingsSessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import com.example.kindred.BuildConfig
import com.example.kindred.DataModels.Book
import kotlin.collections.emptyList


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
        com.example.kindred.SupabaseClient.supabase.auth.signInWith(Email) {
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
    com.example.kindred.SupabaseClient.supabase.auth.signOut()
}


/**
 * Sends book data to Supabase
 */
suspend fun sendBookData (book: Book) {
    SupabaseClient.supabase.postgrest["books"]
        .insert(book)
}

suspend fun getBooks(): List<Book> {
    return SupabaseClient.supabase.postgrest["books"]
        .select()
        .decodeList<Book>()
}