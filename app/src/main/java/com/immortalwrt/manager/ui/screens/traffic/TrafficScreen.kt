package com.immortalwrt.manager.ui.screens.traffic

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.immortalwrt.manager.ui.components.TrafficGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrafficScreen(routerId: String, viewModel: TrafficViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(routerId) { viewModel.loadInterfaces(routerId) }
    DisposableEffect(Unit) { onDispose { viewModel.stopPolling() } }

    Scaffold(topBar = { TopAppBar(title = { Text("Traffic") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (state.interfaces.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = state.selectedInterface,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Interface") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        state.interfaces.forEach { iface ->
                            DropdownMenuItem(
                                text = { Text(iface.displayName) },
                                onClick = {
                                    viewModel.selectInterface(iface.logicalName)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.startPolling(routerId, state.selectedInterface) },
                        enabled = !state.isPolling && state.selectedInterface.isNotBlank()
                    ) { Text("Start") }
                    Button(
                        onClick = { viewModel.stopPolling() },
                        enabled = state.isPolling
                    ) { Text("Stop") }
                }
            }

            Spacer(Modifier.height(8.dp))

            TrafficGraph(dataPoints = state.dataPoints)
        }
    }
}
