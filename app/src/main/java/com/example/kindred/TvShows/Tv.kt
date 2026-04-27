package com.example.kindred.TvShows

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import com.example.kindred.DataModels.TvShow
import com.example.kindred.Supabase.deleteTvShow
import com.example.kindred.Supabase.deleteTvShowSuggestion
import com.example.kindred.Supabase.getTvShowSuggestions
import com.example.kindred.Supabase.getTvShows
import kotlinx.coroutines.launch
import com.example.kindred.DataModels.TvShowSuggestion

/**
 * Composable function which displays the tv screen.
 *
 * @param NavHostController The navigation controller for the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TV(navController: NavController) {

    var isRefreshing by remember { mutableStateOf(false) }
    var tvShows by remember { mutableStateOf<List<TvShow>>(emptyList()) }
    var suggestions by remember { mutableStateOf<List<TvShowSuggestion>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Watchlist", "Watched", "Suggestions")

    LaunchedEffect(Unit) {
        tvShows = getTvShows()
        suggestions = getTvShowSuggestions()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TV Shows") },
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
                    FloatingActionButton(onClick = { navController.navigate("tvShowSuggestions") }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Get Suggestions")
                    }
                } else {
                    FloatingActionButton(onClick = { navController.navigate("addTvShow") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add TV Show")
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
                                suggestions = getTvShowSuggestions()
                            } else {
                                tvShows = getTvShows()
                            }
                            isRefreshing = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
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
                                                deleteTvShowSuggestion(suggestion.suggestion_id!!)
                                                suggestions = getTvShowSuggestions()
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
                                    TvSuggestionCard(suggestedTvShow = suggestion)
                                }
                            }
                        }
                    } else {
                        LazyColumn {
                            items(
                                tvShows.filter {
                                    if (selectedTab == 0) it.status == "watchlist"
                                    else it.status == "watched"
                                },
                                key = { it.tv_show_id!! }
                            ) { tvShow ->
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = { direction ->
                                        if (direction == SwipeToDismissBoxValue.EndToStart) {
                                            coroutineScope.launch {
                                                deleteTvShow(tvShow.tv_show_id!!)
                                                tvShows = getTvShows()
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
                                    TvShowCard(tvShow = tvShow)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
