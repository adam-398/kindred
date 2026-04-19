package com.example.kindred.TvShows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kindred.ConfirmationMessage
import com.example.kindred.DataModels.Movie
import com.example.kindred.SupabaseClient.supabase
import com.example.kindred.sendMovieData
import com.example.kindred.ui.theme.MyChipColor
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTvShow(navController: NavController) {
    var tvTitle by remember { mutableStateOf("") }
    var tvDirector by remember { mutableStateOf("") }
    var tvWriter by remember { mutableStateOf("") }
    var starring by remember { mutableStateOf("") }
    var tvRating by remember { mutableStateOf(0) }
    var selectedTvGenres by remember { mutableStateOf(setOf<String>()) }
    var tvThemes by remember { mutableStateOf("") }
    var tvNotes by remember { mutableStateOf("") }
    var isFavourite by remember { mutableStateOf(false) }
    var closeAddTvShow by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("watchlist") }


    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }


    val genreOptions = listOf(
        "Fiction", "Non-Fiction", "Sci-fi", "Fantasy", "Horror", "Mystery",
        "Thriller", "Comedy", "Drama", "Apocalyptic", "Dystopian", "Adventure"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 72.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
            shape = RoundedCornerShape(18.dp),
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    IconButton(
                        onClick = { isFavourite = !isFavourite },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favourite",
                            tint = if (isFavourite) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier
                                .size(28.dp)
                                .offset(x = 5.dp),
                        )
                    }
                    Text(
                        text = "Add TV Show",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme
                            .typography.titleLarge,
                        textDecoration = TextDecoration.Underline
                    )
                    IconButton(
                        onClick = { closeAddTvShow = true },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close entity form",
                            modifier = Modifier
                                .size(28.dp)
                                .offset(x = (-5).dp),
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = status == "watchlist",
                        onClick = { status = "watchlist" },
                        label = { Text("Want to watch") },
                        colors = MyChipColor()
                    )
                    FilterChip(
                        selected = status == "watched",
                        onClick = { status = "watched" },
                        label = { Text("Watched") },
                        colors = MyChipColor()
                    )
                }
                OutlinedTextField(
                    value = tvTitle,
                    onValueChange = { tvTitle = it },
                    label = { Text(text = "Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = starring,
                    onValueChange = { starring = it },
                    label = { Text(text = "Starring") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                OutlinedTextField(
                    value = tvDirector,
                    onValueChange = { tvDirector = it },
                    label = { Text(text = "Director") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = tvWriter,
                    onValueChange = { tvWriter = it },
                    label = { Text(text = "Writer") },
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
                        IconButton(onClick = { tvRating = star }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "star",
                                tint = if (star <= tvRating)
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
                            selected = selectedTvGenres.contains(option),
                            onClick = {
                                selectedTvGenres =
                                    if (selectedTvGenres.contains(option)) {
                                        selectedTvGenres - option
                                    } else {
                                        selectedTvGenres + option
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
                                .heightIn(min = 48.dp),
                            colors = MyChipColor()
                        )
                    }
                }
                OutlinedTextField(
                    value = tvThemes,
                    onValueChange = { tvThemes = it },
                    label = { Text(text = "Themes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = tvNotes,
                    onValueChange = { tvNotes = it },
                    label = { Text(text = "Notes") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            sendMovieData(
                                Movie(
                                    user_id = supabase.auth.currentSessionOrNull()?.user?.id,
                                    title = tvTitle,
                                    director = tvDirector,
                                    writer = tvWriter,
                                    starring = starring,
                                    rating = tvRating,
                                    genres = selectedTvGenres.joinToString(", "),
                                    themes = tvThemes,
                                    notes = tvNotes,
                                    status = status,
                                    is_favourite = isFavourite
                                )
                            )
                            isLoading = false
                            navController.popBackStack()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isLoading) "Saving..." else "Save")
                }
            }
        }
    }

    if (closeAddTvShow) {
        ConfirmationMessage(
            title = "Cancel adding TV show?",
            message = "Are you sure you want to close the entity form?",
            confirmString = "Yes",
            dismissString = "No",
            icon = null,
            onConfirm = { navController.popBackStack() },
            onDismiss = { closeAddTvShow = false }
        )
    }
}