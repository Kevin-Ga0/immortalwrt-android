package com.immortalwrt.manager.ui.screens.diagnostics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(routerId: String, viewModel: DiagnosticsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(routerId) { viewModel.load(routerId) }

    Scaffold(topBar = { TopAppBar(title = { Text("Diagnostics") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Router: ${state.router?.name ?: "N/A"}", style = MaterialTheme.typography.titleMedium)
            Text("Host: ${state.router?.host}:${state.router?.port}", style = MaterialTheme.typography.bodyMedium)
            Text("Protocol: ${state.router?.scheme?.name}", style = MaterialTheme.typography.bodyMedium)

            HorizontalDivider()

            Text("Anonymization Options", style = MaterialTheme.typography.titleSmall)
            Row { Checkbox(checked = state.ipAnonymized, onCheckedChange = { viewModel.toggleIpAnon() }); Text("Anonymize IPs", modifier = Modifier.padding(start = 4.dp)) }
            Row { Checkbox(checked = state.macAnonymized, onCheckedChange = { viewModel.toggleMacAnon() }); Text("Anonymize MACs", modifier = Modifier.padding(start = 4.dp)) }
            Row { Checkbox(checked = state.hostnameHidden, onCheckedChange = { viewModel.toggleHostnameHidden() }); Text("Hide Hostnames", modifier = Modifier.padding(start = 4.dp)) }

            Button(onClick = { viewModel.generateReport() }, modifier = Modifier.fillMaxWidth()) { Text("Generate Report") }

            if (state.reportContent.isNotEmpty()) {
                HorizontalDivider()
                Text("Report Preview", style = MaterialTheme.typography.titleSmall)
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
                    Text(state.reportContent, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
