package com.example.kindred

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Reusable confirmation screen
 * @param title The title of the screen
 * @param message The message to display
 * @param confirmString to display on the confirm button
 * @param dismissString String to display on the dismiss button
 * @param icon Optional icon to display on the screen
 * @param onConfirm Invoked when the user taps the confirm button
 * @param onDismiss Invoked when the user taps the dismiss button
 */
@Composable
fun ConfirmationMessage(
    title: String,
    message: String? = null,
    confirmString: String,
    dismissString: String,
    icon: ImageVector? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                )
                if (message != null) {
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        25.dp,
                        Alignment.CenterHorizontally
                    ),
                ) {
                    Button(
                        modifier = Modifier.weight(0.5f),
                        onClick = { onConfirm() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = confirmString)
                    }
                    Button(
                        modifier = Modifier.weight(0.5f),
                        onClick = { onDismiss() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = dismissString)
                    }
                }
            }
        }
    }
}