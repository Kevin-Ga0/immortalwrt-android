package com.immortalwrt.manager.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.domain.model.LoadableUiState
import com.immortalwrt.manager.domain.model.Router
import com.immortalwrt.manager.domain.model.SystemInfo
import com.immortalwrt.manager.domain.repository.RouterRepository
import com.immortalwrt.manager.domain.repository.SystemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val systemInfo: SystemInfo? = null,
    val router: Router? = null,
    val uiState: LoadableUiState<Unit> = LoadableUiState.Idle
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val systemRepository: SystemRepository,
    private val routerRepository: RouterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    fun load(routerId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(uiState = LoadableUiState.Loading)
            val router = routerRepository.getById(routerId)
            _state.value = _state.value.copy(router = router)
            val cached = systemRepository.getCachedSystemInfo(routerId)
            if (cached != null) {
                _state.value = _state.value.copy(
                    systemInfo = cached,
                    uiState = LoadableUiState.Content(Unit, isCached = true)
                )
            } else {
                _state.value = _state.value.copy(
                    uiState = LoadableUiState.Empty("No data. Connect to router to refresh.")
                )
            }
        }
    }
}
