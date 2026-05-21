package com.immortalwrt.manager.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.ui.components.ErrorState
import com.immortalwrt.manager.ui.components.StatusCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    routerId: String,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(routerId) {
        viewModel.load(routerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(state.router?.name ?: "Dashboard") })
        }
    ) { padding ->
        when (val s = state.uiState) {
            is LoadableUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadableUiState.Content, is LoadableUiState.Empty -> {
                val info = state.systemInfo
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(padding)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { StatusCard("Model", info?.model ?: "—") }
                    item { StatusCard("Firmware", info?.firmwareVersion ?: "—") }
                    item { StatusCard("Kernel", info?.kernelVersion ?: "—") }
                    item { StatusCard("Architecture", info?.architecture ?: "—") }
                    item {
                        StatusCard(
                            "Uptime",
                            info?.uptime?.let { formatUptime(it) } ?: "—"
                        )
                    }
                    item {
                        val load = info?.loadAverage
                        StatusCard(
                            "Load Average",
                            if (load != null && load.size >= 3) {
                                "%.2f %.2f %.2f".format(load[0], load[1], load[2])
                            } else {
                                "—"
                            }
                        )
                    }
                    item {
                        val mem = if (info != null) {
                            "${formatBytes(info.memoryTotal - info.memoryFree)} / ${formatBytes(info.memoryTotal)}"
                        } else {
                            "—"
                        }
                        StatusCard("Memory Used", mem)
                    }
                    item {
                        StatusCard("Temperature", info?.temperature ?: "—")
                    }
                }
            }

            is LoadableUiState.Error -> {
                ErrorState(
                    message = "Failed to load dashboard",
                    onRetry = { viewModel.load(routerId) },
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {}
        }
    }
}

private fun formatUptime(seconds: Long): String {
    val days = seconds / 86400
    val hours = (seconds % 86400) / 3600
    val mins = (seconds % 3600) / 60
    return if (days > 0) "${days}d ${hours}h" else "${hours}h ${mins}m"
}

private fun formatBytes(bytes: Long): String {
    val gb = bytes / 1024 / 1024 / 1024
    val mb = bytes / 1024 / 1024
    return if (gb > 0) "${gb}G" else "${mb}M"
}
