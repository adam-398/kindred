package com.example.kindred.Books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kindred.DataModels.Book
import com.example.kindred.getBooks


/**
 * Composable function which displays the book suggestion flow.
 * Consists of 3 stages
 * 1st the user selects 5 books to use for suggestions
 * 2nd the user selects the attributes they want to use for suggestions
 * 3rd the user orders the attributes
 *
 * @param navController to navigate to.
 */
@Composable
fun BookSuggestionFlow(navController: NavController) {
    var step by remember { mutableStateOf(1) }
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var selectedBooks by remember { mutableStateOf(setOf<Book>()) }
    var selectedAttributes by remember { mutableStateOf(setOf<String>()) }
    var orderedAttributes by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        books = getBooks().filter { it.status == "read" }
    }

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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (step) {
                    1 -> { /* Step 1 - select books */ }
                    2 -> { /* Step 2 - select attributes */ }
                    3 -> { /* Step 3 - order attributes */ }
                }
            }
        }
    }
}