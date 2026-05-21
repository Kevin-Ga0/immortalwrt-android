package com.immortalwrt.manager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorState(message: String, onRetry: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) { Text("Retry") }
            }
        }
    }
}
