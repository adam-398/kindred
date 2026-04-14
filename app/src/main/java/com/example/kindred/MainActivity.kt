package com.example.kindred

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kindred.SupabaseClient.supabase
import com.example.kindred.ui.theme.KindredTheme
import io.github.jan.supabase.auth.auth
/**
 * MainActivity is the entry point of the application. It sets up the UI and navigation.
 * It also initializes the Supabase client.
 * SupabaseClient is initialized here as it is the earliest point where there is valid Context.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initializes the Supabase client singleton, passing it context so it can set up SharedPreferences for session storage
        SupabaseClient.initialize(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            KindredTheme {
                val navController = rememberNavController()

                var startDestination by remember { mutableStateOf<String?>(value = null) }

                LaunchedEffect(Unit) {
                    supabase.auth.awaitInitialization()
                    startDestination =
                        if (supabase.auth.currentSessionOrNull() != null) "landing" else "login"
                }
                if (startDestination != null) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    ) {
                        composable("login") {
                            Login(navController)
                        }

                        composable("landing") {
                            Landing(navController)
                        }
                        composable("books"){
                            Books(navController)
                        }
                        composable("audiobooks"){
                            Audiobooks(navController)
                        }
                        composable("movies"){
                            Movies(navController)
                        }
                        composable("tv"){
                            TV(navController)
                        }
                        composable("addBook"){
                            AddBook(navController)
                        }
                    }
                }
            }
        }
    }
}

