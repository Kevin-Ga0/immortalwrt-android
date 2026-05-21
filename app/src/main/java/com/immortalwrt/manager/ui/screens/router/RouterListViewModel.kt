package com.immortalwrt.manager.ui.screens.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.domain.model.Router
import com.immortalwrt.manager.domain.repository.RouterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterListViewModel @Inject constructor(
    private val routerRepository: RouterRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoadableUiState<List<Router>>>(LoadableUiState.Idle)
    val state: StateFlow<LoadableUiState<List<Router>>> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = LoadableUiState.Loading
            routerRepository.observeAll().collect { routers ->
                _state.value = if (routers.isEmpty()) {
                    LoadableUiState.Empty("No routers configured")
                } else {
                    LoadableUiState.Content(routers)
                }
            }
        }
    }

    fun deleteRouter(routerId: String) {
        viewModelScope.launch {
            routerRepository.deleteRouter(routerId)
        }
    }
}
