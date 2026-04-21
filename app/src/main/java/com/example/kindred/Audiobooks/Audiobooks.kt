package com.example.kindred.Audiobooks

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kindred.DataModels.Audiobook
import com.example.kindred.DataModels.AudiobookSuggestion
import com.example.kindred.GeminiAPI.getGeminiSuggestions
import com.example.kindred.deleteAudiobook
import com.example.kindred.deleteAudiobookSuggestion
import com.example.kindred.getAudiobookSuggestions
import com.example.kindred.getAudiobooks
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioBooks(navController: NavController) {

    var isRefreshing by remember { mutableStateOf(false) }
    var audiobooks by remember { mutableStateOf<List<Audiobook>>(emptyList()) }
    var suggestions by remember { mutableStateOf<List<AudiobookSuggestion>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Wishlist", "Listened to", "Suggestions", "Completed suggestions")

    LaunchedEffect(Unit) {
        audiobooks = getAudiobooks()
        suggestions = getAudiobookSuggestions()


//        try {
//            val result = getGeminiSuggestions(
//                entities = "the road, commune, after it happened",
//                attributes = "post-apocalyptic, dark, end of the world, narrator, author",
//                genres = "sci-fi, fiction",
//                order = 1
//            )
//            Log.d("GeminiText", result)
//        } catch (e: Exception) {
//            Log.e("GeminiError", "Error: ${e.message}")
//            e.printStackTrace()
//
//        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Audiobooks") },
                    modifier = Modifier.height(100.dp),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedTab == 2) {
                    FloatingActionButton(onClick = { navController.navigate("audiobookSuggestions") }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Get Suggestions")
                    }
                } else {
                    FloatingActionButton(onClick = { navController.navigate("addAudiobook") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Audiobook")
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        coroutineScope.launch {
                            isRefreshing = true
                            if (selectedTab == 2) {
                                suggestions = getAudiobookSuggestions()
                            } else {
                                audiobooks = getAudiobooks()
                            }
                            isRefreshing = false
                        }
                    },
                ) {
                    if (selectedTab == 2) {
                        LazyColumn {
                            items(
                                suggestions,
                                key = { it.suggestion_id!! }
                            ) { suggestion ->
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = { direction ->
                                        if (direction == SwipeToDismissBoxValue.EndToStart) {
                                            coroutineScope.launch {
                                                deleteAudiobookSuggestion(suggestion.suggestion_id!!)
                                                suggestions = getAudiobookSuggestions()
                                            }
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                )
                                SwipeToDismissBox(
                                    state = dismissState,
                                    enableDismissFromStartToEnd = false,
                                    backgroundContent = {
                                        val color by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                                else -> Color.Transparent
                                            }, label = "delete_anim"
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .background(color, MaterialTheme.shapes.medium),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                modifier = Modifier.padding(end = 24.dp),
                                                tint = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }
                                ) {
                                    AudiobookSuggestionCard(suggestedAudiobook = suggestion)
                                }
                            }
                        }
                    } else {
                        LazyColumn {
                            items(
                                audiobooks.filter {
                                    if (selectedTab == 0) it.status == "wishlist"
                                    else it.status == "listened to"
                                },
                                key = { it.book_id!! }
                            ) { audiobook ->
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = { direction ->
                                        if (direction == SwipeToDismissBoxValue.EndToStart) {
                                            coroutineScope.launch {
                                                deleteAudiobook(audiobook.book_id!!)
                                                audiobooks = getAudiobooks()
                                            }
                                            true
                                        } else {
                                            false
                                        }
                                    }
                                )
                                SwipeToDismissBox(
                                    state = dismissState,
                                    enableDismissFromStartToEnd = false,
                                    backgroundContent = {
                                        val color by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                                else -> Color.Transparent
                                            }, label = "delete_anim"
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .background(color, MaterialTheme.shapes.medium),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                modifier = Modifier.padding(end = 24.dp),
                                                tint = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }
                                ) {
                                    AudiobookCard(audiobook = audiobook)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}