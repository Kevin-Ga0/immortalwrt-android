package com.immortalwrt.manager.ui.screens.reboot

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RebootScreen(routerId: String, viewModel: RebootViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(routerId) { viewModel.load(routerId) }

    Scaffold(topBar = { TopAppBar(title = { Text("Reboot Router") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (state.state) {
                RebootState.IDLE -> {
                    Text("Router: ${state.routerName}", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(24.dp))
                    Text("Reboot the router. Internet connection will be temporarily unavailable.", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.startReboot(routerId) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                        Text("Reboot Router")
                    }
                }
                RebootState.CONFIRMING -> {
                    Text("Confirm Reboot", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Text("Are you sure you want to reboot ${state.routerName}?\n\nAll network connections will be interrupted.", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = viewModel::cancel) { Text("Cancel") }
                        Button(onClick = { viewModel.confirmReboot(routerId) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                            Text("Confirm Reboot")
                        }
                    }
                }
                RebootState.REBOOT_COMMAND_SENT, RebootState.REBOOT_COMMAND_POSSIBLY_ACCEPTED,
                RebootState.WAITING_OFFLINE, RebootState.WAITING_ONLINE, RebootState.RECONNECTING -> {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text(state.message, style = MaterialTheme.typography.bodyLarge)
                    if (state.elapsedSeconds > 0) {
                        Spacer(Modifier.height(8.dp))
                        Text("${state.elapsedSeconds}s elapsed", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                RebootState.SUCCESS -> {
                    Text("Router is back online", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Text(state.message, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = viewModel::reset) { Text("Done") }
                }
                RebootState.FAILED -> {
                    Text("Reboot failed", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Text(state.message, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = viewModel::reset) { Text("OK") }
                }
                RebootState.CANCELLED -> {
                    Text("Reboot cancelled", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = viewModel::reset) { Text("Back") }
                }
            }
        }
    }
}
