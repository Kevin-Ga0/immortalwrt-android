package com.immortalwrt.manager.ui.screens.reboot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.core.security.SecretStore
import com.immortalwrt.manager.core.session.SessionManager
import com.immortalwrt.manager.domain.model.*
import com.immortalwrt.manager.domain.repository.RouterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class RebootState {
    IDLE, CONFIRMING, REBOOT_COMMAND_SENT, REBOOT_COMMAND_POSSIBLY_ACCEPTED,
    WAITING_OFFLINE, WAITING_ONLINE, RECONNECTING, SUCCESS, FAILED, CANCELLED
}

data class RebootUiState(
    val state: RebootState = RebootState.IDLE,
    val routerName: String = "",
    val message: String = "",
    val elapsedSeconds: Int = 0
)

@HiltViewModel
class RebootViewModel @Inject constructor(
    private val rpcClient: UbusJsonRpcClient,
    private val sessionManager: SessionManager,
    private val secretStore: SecretStore,
    private val routerRepository: RouterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RebootUiState())
    val state: StateFlow<RebootUiState> = _state.asStateFlow()

    fun startReboot(routerId: String) {
        viewModelScope.launch {
            _state.update { it.copy(state = RebootState.CONFIRMING) }
        }
    }

    fun confirmReboot(routerId: String) {
        viewModelScope.launch {
            _state.update { it.copy(state = RebootState.REBOOT_COMMAND_SENT, message = "Sending reboot command...") }
            val router = routerRepository.getById(routerId) ?: run {
                _state.update { it.copy(state = RebootState.FAILED, message = "Router not found") }
                return@launch
            }
            val sessionResult = sessionManager.getSession(routerId)
            if (sessionResult !is AppResult.Success) {
                _state.update { it.copy(state = RebootState.FAILED, message = "Not authenticated") }
                return@launch
            }
            val session = sessionResult.data
            val endpoint = router.endpoint ?: "/ubus"
            val securityContext = RouterSecurityContext(
                routerId = routerId, scheme = router.scheme, host = router.host,
                port = router.port, certificateFingerprint = router.certificateFingerprint,
                httpRiskAccepted = router.scheme == RouterScheme.HTTP
            )

            try {
                val result = rpcClient.call(securityContext, session.sessionId, endpoint, "system", "reboot", emptyMap())
                when (result) {
                    is AppResult.Success -> {
                        _state.update { it.copy(state = RebootState.WAITING_OFFLINE, message = "Reboot command sent. Waiting for router to go offline...") }
                    }
                    is AppResult.Failure -> {
                        val error = result.error
                        if (error is AppError.Timeout || error is AppError.NetworkUnavailable) {
                            _state.update { it.copy(state = RebootState.REBOOT_COMMAND_POSSIBLY_ACCEPTED, message = "Connection lost after reboot command. Router likely rebooting.") }
                        } else {
                            _state.update { it.copy(state = RebootState.FAILED, message = "Reboot command failed: ${error}") }
                            return@launch
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(state = RebootState.REBOOT_COMMAND_POSSIBLY_ACCEPTED, message = "Request sent but connection interrupted. Router likely rebooting.") }
            }

            // Wait for offline
            if (_state.value.state == RebootState.WAITING_OFFLINE || _state.value.state == RebootState.REBOOT_COMMAND_POSSIBLY_ACCEPTED) {
                _state.update { it.copy(state = RebootState.WAITING_OFFLINE) }
                // Wait up to 30s for offline
                for (i in 0..30) {
                    delay(1000)
                    _state.update { it.copy(elapsedSeconds = i + 1) }
                    if (i >= 30) {
                        // Timeout waiting for offline - assume rebooting
                        _state.update { it.copy(state = RebootState.WAITING_ONLINE, message = "Waiting for router to come back online...") }
                    }
                }
                // Wait for online (up to 180s)
                _state.update { it.copy(state = RebootState.WAITING_ONLINE, message = "Waiting for router to come back online...", elapsedSeconds = 0) }
                for (i in 0..180) {
                    delay(1000)
                    _state.update { it.copy(elapsedSeconds = i + 1) }
                    if (i >= 180) {
                        _state.update { it.copy(state = RebootState.FAILED, message = "Timeout waiting for router to come online. Please check manually.") }
                        return@launch
                    }
                }
                // Reconnecting
                _state.update { it.copy(state = RebootState.RECONNECTING, message = "Attempting to reconnect...") }
                val pwBytes = secretStore.retrieve("router_${routerId}_password")
                val password = pwBytes?.toString(Charsets.UTF_8) ?: ""
                if (password.isEmpty()) {
                    _state.update { it.copy(state = RebootState.FAILED, message = "Password not found. Please re-enter credentials manually.") }
                    return@launch
                }
                for (attempt in 1..3) {
                    val loginResult = sessionManager.login(routerId, securityContext, endpoint, router.username, password)
                    if (loginResult is AppResult.Success) {
                        _state.update { it.copy(state = RebootState.SUCCESS, message = "Reboot complete. Router is back online.") }
                        return@launch
                    }
                    delay(5000)
                }
                _state.update { it.copy(state = RebootState.FAILED, message = "Failed to reconnect after reboot. Please try logging in manually.") }
            }
        }
    }

    fun cancel() {
        _state.update { it.copy(state = RebootState.CANCELLED) }
    }

    fun reset() {
        _state.update { it.copy(state = RebootState.IDLE, message = "", elapsedSeconds = 0) }
    }

    fun load(routerId: String) {
        viewModelScope.launch {
            val router = routerRepository.getById(routerId)
            _state.update { it.copy(routerName = router?.name ?: "") }
        }
    }
}
