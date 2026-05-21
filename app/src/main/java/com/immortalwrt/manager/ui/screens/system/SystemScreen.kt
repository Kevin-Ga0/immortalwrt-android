package com.immortalwrt.manager.ui.screens.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemScreen(
    routerId: String,
    viewModel: SystemViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(routerId) {
        viewModel.load(routerId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("System") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { InfoRow("Hostname", state.systemInfo?.hostname) }
            item { InfoRow("Model", state.systemInfo?.model) }
            item { InfoRow("Architecture", state.systemInfo?.architecture) }
            item { InfoRow("Target Platform", state.systemInfo?.targetPlatform) }
            item { InfoRow("Firmware Version", state.systemInfo?.firmwareVersion) }
            item { InfoRow("Kernel Version", state.systemInfo?.kernelVersion) }
            item {
                InfoRow(
                    "Local Time",
                    state.systemInfo?.localTime?.let {
                        java.text.SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(it * 1000))
                    }
                )
            }
            item {
                InfoRow(
                    "Uptime",
                    state.systemInfo?.uptime?.let { formatUptime(it) }
                )
            }
            item { InfoRow("CPU Usage", state.systemInfo?.cpuUsage) }
            item { InfoRow("Temperature", state.systemInfo?.temperature) }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            item {
                InfoRow(
                    "LuCI Available",
                    state.environment?.luciAvailable?.toString()
                )
            }
            item {
                InfoRow(
                    "rpcd Available",
                    state.environment?.rpcdAvailable?.toString()
                )
            }
            item {
                InfoRow(
                    "Endpoint Mode",
                    state.environment?.endpointMode?.name
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            item {
                state.capability?.let { cap ->
                    Text(
                        "Capabilities",
                        style = MaterialTheme.typography.titleMedium
                    )
                    cap::class.java.declaredFields
                        .filter { it.type == Boolean::class.javaPrimitiveType }
                        .forEach { field ->
                            field.isAccessible = true
                            InfoRow(
                                field.name.removePrefix("has").removePrefix("can"),
                                if (field.getBoolean(cap)) "Available" else "Unavailable"
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(140.dp)
        )
        Text(
            text = value ?: "—",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun formatUptime(seconds: Long): String {
    val days = seconds / 86400
    val hours = (seconds % 86400) / 3600
    val mins = (seconds % 3600) / 60
    return if (days > 0) "${days}d ${hours}h ${mins}m" else "${hours}h ${mins}m"
}
