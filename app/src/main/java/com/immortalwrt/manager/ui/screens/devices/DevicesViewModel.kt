package com.immortalwrt.manager.ui.screens.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.domain.model.*
import com.immortalwrt.manager.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoadableUiState<List<ConnectedDevice>>>(LoadableUiState.Idle)
    val state: StateFlow<LoadableUiState<List<ConnectedDevice>>> = _state.asStateFlow()

    fun load(routerId: String) {
        viewModelScope.launch {
            _state.value = LoadableUiState.Loading
            val cached = deviceRepository.getCachedDevices(routerId)
            if (cached.isNotEmpty()) {
                _state.value = LoadableUiState.Content(cached, isCached = true)
            } else {
                _state.value = LoadableUiState.Empty("No devices. Connect to router to refresh.")
            }
        }
    }
}
