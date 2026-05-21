package com.immortalwrt.manager.ui.screens.router

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.immortalwrt.manager.domain.model.RouterScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRouterScreen(
    routerId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: AddEditRouterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isEditing = routerId != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Router" else "Add Router") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::updateName,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.host,
                onValueChange = viewModel::updateHost,
                label = { Text("Host") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            OutlinedTextField(
                value = state.port,
                onValueChange = viewModel::updatePort,
                label = { Text("Port") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Scheme: ", style = MaterialTheme.typography.bodyLarge)
                FilterChip(
                    selected = state.scheme == RouterScheme.HTTP,
                    onClick = { viewModel.updateScheme(RouterScheme.HTTP) },
                    label = { Text("HTTP") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = state.scheme == RouterScheme.HTTPS,
                    onClick = { viewModel.updateScheme(RouterScheme.HTTPS) },
                    label = { Text("HTTPS") }
                )
            }

            if (state.scheme == RouterScheme.HTTP && !state.httpRiskAccepted) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "HTTP connections are not encrypted. Your password and router data will be sent in cleartext.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = viewModel::acceptHttpRisk,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("I Understand the Risk")
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.endpoint,
                onValueChange = viewModel::updateEndpoint,
                label = { Text("Endpoint (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = viewModel::updateUsername,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.save(routerId)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.name.isNotBlank() &&
                    state.host.isNotBlank() &&
                    state.password.isNotBlank() &&
                    (state.scheme == RouterScheme.HTTPS || state.httpRiskAccepted) &&
                    !state.isSaving
            ) {
                Text(if (isEditing) "Save Changes" else "Add Router")
            }
        }
    }
}
