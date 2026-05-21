package com.immortalwrt.manager.ui.screens.router

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.domain.model.Router
import com.immortalwrt.manager.domain.model.RouterScheme
import com.immortalwrt.manager.ui.components.HttpInsecureBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouterListScreen(
    onAddRouter: () -> Unit,
    onEditRouter: (String) -> Unit,
    onRouterClick: (String) -> Unit,
    viewModel: RouterListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Routers") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRouter) {
                Icon(Icons.Default.Add, contentDescription = "Add router")
            }
        }
    ) { padding ->
        when (val s = state) {
            is LoadableUiState.Idle, is LoadableUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is LoadableUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(s.message, style = MaterialTheme.typography.bodyLarge)
                }
            }
            is LoadableUiState.Content -> {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(s.data, key = { it.id }) { router ->
                        RouterListItem(
                            router = router,
                            onClick = { onRouterClick(router.id) },
                            onEdit = { onEditRouter(router.id) },
                            onDelete = { viewModel.deleteRouter(router.id) }
                        )
                    }
                }
            }
            is LoadableUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Failed to load routers",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.load() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is LoadableUiState.Unsupported -> {}
        }
    }
}

@Composable
private fun RouterListItem(
    router: Router,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        router.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (router.scheme == RouterScheme.HTTP) {
                        Spacer(Modifier.width(8.dp))
                        HttpInsecureBadge()
                    }
                }
                Text(
                    "${router.host}:${router.port}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (router.lastSuccessAt != null) {
                    Text(
                        "Last online: ...",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
