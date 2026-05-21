package com.immortalwrt.manager.ui.screens.devices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.immortalwrt.manager.domain.model.ConnectedDevice
import com.immortalwrt.manager.domain.model.DeviceSource
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.ui.components.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(routerId: String, viewModel: DevicesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(routerId) { viewModel.load(routerId) }

    Scaffold(topBar = { TopAppBar(title = { Text("Devices") }) }) { padding ->
        when (val s = state) {
            is LoadableUiState.Loading -> Box(Modifier.fillMaxSize().padding(padding)) { CircularProgressIndicator() }
            is LoadableUiState.Content, is LoadableUiState.Empty -> {
                val devices = if (s is LoadableUiState.Content) s.data else emptyList()
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(devices, key = { it.mac ?: it.ip ?: it.hashCode().toString() }) { device ->
                        DeviceListItem(device)
                    }
                }
            }
            is LoadableUiState.Error -> ErrorState("Failed to load devices", onRetry = { viewModel.load(routerId) }, modifier = Modifier.padding(padding))
            else -> {}
        }
    }
}

@Composable
private fun DeviceListItem(device: ConnectedDevice) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(device.hostname ?: device.ip ?: "Unknown", style = MaterialTheme.typography.titleMedium)
                Text(device.mac ?: "", style = MaterialTheme.typography.bodySmall)
                Row {
                    device.sources.forEach { source ->
                        Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.padding(end = 4.dp)) {
                            Text(source.name, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
            Surface(
                color = if (device.isOnline == true) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    if (device.isOnline == true) "Online" else "Offline",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
