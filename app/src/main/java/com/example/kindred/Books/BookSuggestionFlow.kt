package com.example.kindred.Books

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kindred.DataModels.Book
import com.example.kindred.DataModels.BookSuggestion
import com.example.kindred.GeminiAPI.BookSuggestionViewModel
import com.example.kindred.Supabase.SupabaseClient.supabase
import com.example.kindred.Supabase.getBooks
import com.example.kindred.Supabase.sendBookSuggestionData
import com.example.kindred.ui.theme.MyChipColor
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


/**
 * Composable function which displays the book suggestion flow.
 * Consists of 3 stages
 * 1st the user selects 5 books to use for suggestions
 * 2nd the user selects the attributes they want to use for suggestions
 * 3rd the user orders the attributes
 *
 * @param navController to navigate to.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSuggestionFlow(navController: NavController) {
    var step by remember { mutableStateOf(1) }
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var selectedBooks by remember { mutableStateOf(setOf<Book>()) }
    var selectedAttributes by remember { mutableStateOf(setOf<String>()) }
    var orderedAttributes by remember { mutableStateOf(listOf<String>()) }
    var chosenSuggestions by remember { mutableStateOf(setOf<BookSuggestion>()) }

    val listState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyListState = listState) { from, to ->
        orderedAttributes = orderedAttributes.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    }
    val viewModel: BookSuggestionViewModel = viewModel()

    val suggestions by viewModel.suggestions.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()




    val attributes = listOf("Author", "Genres", "Themes", "Notes", "Rating")

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        books = getBooks().filter { it.status == "read" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text ("Get Suggestions") },
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
                        text = "Step 1: Select 5 Books",
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
                        items(books) { book ->
                            val isSelected = book in selectedBooks
                            BookSelectCard(
                                book = book,
                                isSelected = isSelected,
                                onClick = {
                                    selectedBooks = if (isSelected) {
                                        selectedBooks - book
                                    } else {
                                        selectedBooks + book
                                    }
                                }
                            )
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.setSelectedBooks(selectedBooks.toList())
                            step = 2
                        },
                        enabled = selectedBooks.isNotEmpty() && selectedBooks.size <= 5,
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
                                val isSelected = suggestion in chosenSuggestions
                                BookSuggestionCard(
                                    suggestedBook = suggestion,
                                    isSelected = isSelected,
                                    onClick = {
                                        chosenSuggestions = if (isSelected) {
                                            chosenSuggestions - suggestion
                                        } else {
                                            chosenSuggestions + suggestion
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                chosenSuggestions.forEach { suggestion ->
                                    sendBookSuggestionData(
                                        suggestion.copy(
                                            user_id = supabase.auth.currentSessionOrNull()?.user?.id,
                                            entity_type = "book"
                                        )
                                    )
                                }
                                navController.popBackStack()
                            }
                        },
                        enabled = chosenSuggestions.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Save books")
                    }
                }
            }
        }
    }
}

