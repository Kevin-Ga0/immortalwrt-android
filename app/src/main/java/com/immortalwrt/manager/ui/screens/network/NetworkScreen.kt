package com.immortalwrt.manager.ui.screens.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.domain.model.NetworkInterface
import com.immortalwrt.manager.ui.components.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(routerId: String, viewModel: NetworkViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(routerId) { viewModel.load(routerId) }

    Scaffold(topBar = { TopAppBar(title = { Text("Network") }) }) { padding ->
        when (val s = state) {
            is LoadableUiState.Loading -> Box(Modifier.fillMaxSize().padding(padding)) { CircularProgressIndicator() }
            is LoadableUiState.Content, is LoadableUiState.Empty -> {
                val ifaces = if (s is LoadableUiState.Content) s.data else emptyList()
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(ifaces) { iface -> InterfaceCard(iface) }
                }
            }
            is LoadableUiState.Error -> ErrorState("Failed to load interfaces", onRetry = { viewModel.load(routerId) }, modifier = Modifier.padding(padding))
            else -> {}
        }
    }
}

@Composable
private fun InterfaceCard(iface: NetworkInterface) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), onClick = { expanded = !expanded }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val statusColor = if (iface.up) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                Surface(color = statusColor, shape = MaterialTheme.shapes.small, modifier = Modifier.size(8.dp)) {}
                Spacer(Modifier.width(8.dp))
                Text(iface.displayName, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(iface.mac ?: "", style = MaterialTheme.typography.bodySmall)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(24.dp))
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    if (iface.ipAddrs.isNotEmpty()) Text("IPv4: ${iface.ipAddrs.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    if (iface.ip6Addrs.isNotEmpty()) Text("IPv6: ${iface.ip6Addrs.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    if (iface.gateway != null) Text("Gateway: ${iface.gateway}", style = MaterialTheme.typography.bodySmall)
                    if (iface.dns.isNotEmpty()) Text("DNS: ${iface.dns.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    Text("RX: ${formatBytes(iface.rxBytes)}  TX: ${formatBytes(iface.txBytes)}", style = MaterialTheme.typography.bodySmall)
                    if (iface.mtu != null) Text("MTU: ${iface.mtu}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    val kb = bytes / 1024; val mb = kb / 1024; val gb = mb / 1024
    return when { gb > 0 -> "${gb}G"; mb > 0 -> "${mb}M"; kb > 0 -> "${kb}K"; else -> "${bytes}B" }
}
