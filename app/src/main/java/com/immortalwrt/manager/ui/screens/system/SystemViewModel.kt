package com.immortalwrt.manager.ui.screens.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.domain.model.RouterCapability
import com.immortalwrt.manager.domain.model.RouterEnvironment
import com.immortalwrt.manager.domain.model.SystemInfo
import com.immortalwrt.manager.domain.repository.CapabilityRepository
import com.immortalwrt.manager.domain.repository.SystemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SystemUiState(
    val systemInfo: SystemInfo? = null,
    val environment: RouterEnvironment? = null,
    val capability: RouterCapability? = null,
    val uiState: LoadableUiState<Unit> = LoadableUiState.Idle
)

@HiltViewModel
class SystemViewModel @Inject constructor(
    private val systemRepository: SystemRepository,
    private val capabilityRepository: CapabilityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SystemUiState())
    val state: StateFlow<SystemUiState> = _state.asStateFlow()

    fun load(routerId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(uiState = LoadableUiState.Loading)
            val info = systemRepository.getCachedSystemInfo(routerId)
            val env = capabilityRepository.getEnvironment(routerId)
            val cap = capabilityRepository.getCapability(routerId)
            _state.value = SystemUiState(
                systemInfo = info,
                environment = env,
                capability = cap,
                uiState = if (info != null) {
                    LoadableUiState.Content(Unit)
                } else {
                    LoadableUiState.Content(Unit, isCached = true)
                }
            )
        }
    }
}
