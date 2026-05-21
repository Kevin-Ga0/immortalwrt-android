package com.immortalwrt.manager.ui.screens.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordChangeScreen(routerId: String, viewModel: PasswordChangeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(routerId) { viewModel.load(routerId) }

    Scaffold(topBar = { TopAppBar(title = { Text("Change Password") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Router: ${state.routerName}", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = state.newPassword, onValueChange = viewModel::updateNewPassword,
                label = { Text("New Password") }, modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            OutlinedTextField(
                value = state.confirmPassword, onValueChange = viewModel::updateConfirmPassword,
                label = { Text("Confirm Password") }, modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                onClick = { viewModel.changePassword(routerId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.newPassword.isNotBlank() && state.newPassword == state.confirmPassword && !state.isChanging
            ) {
                if (state.isChanging) CircularProgressIndicator(modifier = Modifier.size(16.dp)) else Text("Change Password")
            }

            state.result?.let { result ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (result) {
                            is PasswordChangeResult.Success -> MaterialTheme.colorScheme.primaryContainer
                            is PasswordChangeResult.Unsupported -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.errorContainer
                        }
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        when (result) {
                            is PasswordChangeResult.Success -> Text("Password changed successfully.")
                            is PasswordChangeResult.Failed -> Text("Failed: ${result.message}")
                            is PasswordChangeResult.ReLoginRequired -> Text(result.message)
                            is PasswordChangeResult.Unsupported -> Text("Password change is not supported on this router. Use LuCI or SSH.")
                        }
                    }
                }
            }
        }
    }
}
