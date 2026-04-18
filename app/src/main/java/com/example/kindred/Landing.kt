package com.example.kindred

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch


/**
 * Composable function which displays the landing screen.
 *
 * @param NavHostController The navigation controller for the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Landing(navController: NavController) {
    val importViewModel: ImportViewModel = viewModel(LocalContext.current as ComponentActivity)
    val coroutineScope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var importMode by remember { mutableStateOf("none") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val allItems = parseAudibleJson(context, it)

            val filteredList = when (importMode) {
                "wishlist" -> {
                    allItems.filter { item ->
                        val tags = item.collectionIds ?: emptyList()
                        tags.contains("__WISHLIST") || !tags.contains("__AYCL")
                    }
                }
                "library" -> {
                    allItems.filter { item ->
                        item.collectionIds?.contains("__AYCL") == true
                    }
                }
                else -> allItems
            }

            importViewModel.setImportStatus(if (importMode == "wishlist") "wishlist" else "read")
            importViewModel.setWishList(filteredList)
            navController.navigate("importPreview")
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Kindred") },
                    modifier = Modifier.height(100.dp),
                    actions = {
                        IconButton(onClick = { showLogoutDialog = true }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ElevatedCard(
                        onClick = { navController.navigate("books") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(width = 240.dp, height = 100.dp)
                    ) {
                        Text(
                            text = "Books",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                        )
                    }
                    ElevatedCard(
                        onClick = { navController.navigate("audiobooks") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(width = 240.dp, height = 100.dp)
                    ) {
                        Text(
                            text = "Audiobooks",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                        )
                    }
                    ElevatedCard(
                        onClick = { navController.navigate("movies") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(width = 240.dp, height = 100.dp)
                    ) {
                        Text(
                            text = "Movies",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                        )
                    }
                    ElevatedCard(
                        onClick = { navController.navigate("tv") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(width = 240.dp, height = 100.dp)
                    ) {
                        Text(
                            text = "TV",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                        )
                    }
                    ElevatedCard(
                        onClick = {
                            importMode = "wishlist"
                            launcher.launch("application/json")
                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(width = 240.dp, height = 100.dp)
                    ) {
                        Text(
                            text = "Import wishlist",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                        )
                    }
                    ElevatedCard(
                        onClick = {
                            importMode = "library"
                            launcher.launch("application/json")
                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(width = 240.dp, height = 100.dp)
                    ) {
                        Text(
                            text = "Import library",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .wrapContentSize(Alignment.Center),
                        )
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        ConfirmationMessage(
            title = "Logout",
            message = "Are you sure you want to logout?",
            confirmString = "Logout",
            dismissString = "Cancel",
            icon = null,
            onConfirm = {
                coroutineScope.launch {
                    logoutUser()
                    navController.navigate("login") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}