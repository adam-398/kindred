package com.example.kindred

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddBook(navController: NavController) {
    var bookTitle by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }
    var bookRating by remember { mutableStateOf(0) }
    var selectedBookGenres by remember { mutableStateOf(setOf<String>()) }
    var bookTheme by remember { mutableStateOf("") }
    var bookNotes by remember { mutableStateOf("") }

    val genreOptions = listOf(
        "Fiction", "Non-Fiction", "Sci-fi", "Fantasy", "Horror", "Mystery",
        "Romance", "Thriller", "Comedy", "Drama", "Apocalyptic", "Dystopian", "Adventure"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.85f)
                    .padding(8.dp),
                shape = RoundedCornerShape(18.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add Book",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                    )
                    OutlinedTextField(
                        value = bookTitle,
                        onValueChange = { bookTitle = it },
                        label = { Text(text = "Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = bookAuthor,
                        onValueChange = { bookAuthor = it },
                        label = { Text(text = "Author") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                    Text(
                        text = "Rating",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 4.dp)
                    )
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { bookRating = star }) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "star",
                                    tint = if (star <= bookRating)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        Color.Gray
                                )
                            }
                        }
                    }
                    Text(
                        text = "Genre",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 4.dp)
                    )
                    FlowRow(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genreOptions.forEach { option ->
                            FilterChip(
                                selected = selectedBookGenres.contains(option),
                                onClick = {
                                    selectedBookGenres = if (selectedBookGenres.contains(option)) {
                                        selectedBookGenres - option
                                    } else {
                                        selectedBookGenres + option
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
                                    .width(120.dp)
                                    .heightIn(min = 48.dp)
                            )
                        }
                    }
                    OutlinedTextField(
                        value = bookTheme,
                        onValueChange = { bookTheme = it },
                        label = { Text(text = "Theme") },
                        minLines = 2,
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                    OutlinedTextField(
                        value = bookNotes,
                        onValueChange = { bookNotes = it },
                        label = { Text(text = "Notes") },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                    Button(
                        onClick = { /* save logic */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}