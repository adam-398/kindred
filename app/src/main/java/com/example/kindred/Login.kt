package com.example.kindred

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


/**
 * Composable function which displays the login screen.
 *
 * @param NavHostController The navigation controller for the app.
 */
@Composable
fun Login(navHostController: NavHostController) {
    val coRoutineScope = rememberCoroutineScope()
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = emailState,
                onValueChange = {
                    emailState = it
                    errorMessage = ""
                },
                label =
                    { Text(text = "Email") },
                modifier = Modifier
                    .semantics { contentType = ContentType.EmailAddress }
                    .padding(all = 10.dp)
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = {
                    passwordState = it
                    errorMessage = ""
                },
                label =
                    { Text(text = "Password")},
                modifier = Modifier
                    .semantics { contentType = ContentType.Password }
                    .padding(all = 10.dp)
            )


        }


    }
}






