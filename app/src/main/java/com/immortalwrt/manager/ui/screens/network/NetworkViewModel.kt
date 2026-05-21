package com.immortalwrt.manager.ui.screens.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.domain.model.*
import com.immortalwrt.manager.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoadableUiState<List<NetworkInterface>>>(LoadableUiState.Idle)
    val state: StateFlow<LoadableUiState<List<NetworkInterface>>> = _state.asStateFlow()

    fun load(routerId: String) {
        viewModelScope.launch {
            _state.value = LoadableUiState.Loading
            val cached = networkRepository.getCachedInterfaces(routerId)
            if (cached.isNotEmpty()) {
                _state.value = LoadableUiState.Content(cached, isCached = true)
            } else {
                _state.value = LoadableUiState.Empty("No interface data. Connect to router to refresh.")
            }
        }
    }
}
