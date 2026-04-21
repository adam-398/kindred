package com.example.kindred.Audiobooks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kindred.DataModels.Audiobook
import com.example.kindred.GeminiAPI.AudiobookSuggestionViewModel
import com.example.kindred.getAudiobooks
import com.example.kindred.ui.theme.MyChipColor
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


/**
 * Composable function which displays the audiobook suggestion flow.
 * Consists of 3 stages
 * 1st the user selects 5 audiobooks to use for suggestions
 * 2nd the user selects the attributes they want to use for suggestions
 * 3rd the user orders the attributes
 *
 * @param navController to navigate to.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AudiobookSuggestionFlow(navController: NavController) {
    var step by remember { mutableStateOf(1) }
    var audiobooks by remember { mutableStateOf<List<Audiobook>>(emptyList()) }
    var selectedAudiobooks by remember { mutableStateOf(setOf<Audiobook>()) }

    var selectedAttributes by remember { mutableStateOf(setOf<String>()) }
    var orderedAttributes by remember { mutableStateOf(listOf<String>()) }

    val listState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyListState = listState) { from, to ->
        orderedAttributes = orderedAttributes.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }

    val viewModel: AudiobookSuggestionViewModel = viewModel()

    val attributes = listOf("Author", "Narrator", "Genres", "Themes", "Notes", "Rating")

    val suggestions by viewModel.suggestions.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        audiobooks = getAudiobooks().filter { it.status == "listened to" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Get Suggestions") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (step) {
                1 -> {
                    Text(
                        text = "Step 1: Select 5 Audiobooks",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(audiobooks) { audiobook ->
                            val isSelected = audiobook in selectedAudiobooks
                            AudiobookSelectCard(
                                audiobook = audiobook,
                                isSelected = isSelected,
                                onClick = {
                                    selectedAudiobooks = if (isSelected) {
                                        selectedAudiobooks - audiobook
                                    } else {
                                        selectedAudiobooks + audiobook
                                    }
                                }
                            )
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.setSelectedAudiobooks(selectedAudiobooks.toList())
                            step = 2
                        },
                        enabled = selectedAudiobooks.isNotEmpty() && selectedAudiobooks.size <= 5,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Next")
                    }
                }

                2 -> {
                    Text(
                        text = "Step 2: Select 3 or more attributes",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    FlowColumn(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        attributes.forEach { option ->
                            FilterChip(
                                selected = selectedAttributes.contains(option),
                                onClick = {
                                    selectedAttributes =
                                        if (selectedAttributes.contains(option)) {
                                            selectedAttributes - option
                                        } else {
                                            selectedAttributes + option
                                        }
                                },
                                label = {
                                    Text(
                                        text = option,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                modifier = Modifier
                                    .padding(all = 10.dp)
                                    .heightIn(min = 48.dp),
                                colors = MyChipColor()
                            )
                        }
                    }

                    Button(
                        onClick = {
                            orderedAttributes = selectedAttributes.toList()
                            viewModel.setSelectedAttributes(selectedAttributes.toList())
                            step = 3
                        },
                        enabled = selectedAttributes.size >= 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Next")
                    }
                }

                3 -> {
                    Text(
                        text = "Step 3: Reorder attributes in order of importance to you",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        items(orderedAttributes, key = { it }) { attribute ->
                            ReorderableItem(reorderState, key = attribute) {
                                Row {
                                    Text(
                                        text = attribute,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(16.dp)
                                    )
                                    IconButton(
                                        modifier = Modifier.draggableHandle(),
                                        onClick = {}
                                    ) {
                                        Icon(Icons.Default.DragHandle, contentDescription = "Drag")
                                    }
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.setAttributeOrder(orderedAttributes)
                            viewModel.fetchSuggestions()
                            step = 4
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Get suggestions")
                    }

                }

                4 -> {
                    if (loading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (error != null) {
                        Text("Error: $error")
                    } else {
                        LazyColumn {
                            items(suggestions) { suggestion ->
                                AudiobookSuggestionCard(suggestedAudiobook = suggestion)
                            }
                        }
                    }


                }
            }
        }
    }
}