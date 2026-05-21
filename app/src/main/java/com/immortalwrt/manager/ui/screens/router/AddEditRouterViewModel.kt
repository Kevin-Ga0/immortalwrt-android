package com.immortalwrt.manager.ui.screens.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.core.network.EndpointProbeResult
import com.immortalwrt.manager.core.network.Layer2Result
import com.immortalwrt.manager.core.network.Layer3Result
import com.immortalwrt.manager.domain.model.AppResult
import com.immortalwrt.manager.domain.model.Router
import com.immortalwrt.manager.domain.model.RouterConnectionState
import com.immortalwrt.manager.domain.model.RouterScheme
import com.immortalwrt.manager.domain.repository.RouterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddRouterUiState(
    val name: String = "",
    val host: String = "",
    val port: String = "80",
    val scheme: RouterScheme = RouterScheme.HTTP,
    val endpoint: String = "",
    val username: String = "root",
    val password: String = "",
    val httpRiskAccepted: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val hostError: String? = null,
    val portError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val connectionState: RouterConnectionState = RouterConnectionState.UNKNOWN,
    val probeResult: EndpointProbeResult? = null
)

@HiltViewModel
class AddEditRouterViewModel @Inject constructor(
    private val routerRepository: RouterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AddRouterUiState())
    val state: StateFlow<AddRouterUiState> = _state.asStateFlow()

    fun updateName(name: String) = _state.update { it.copy(name = name, nameError = null) }
    fun updateHost(host: String) = _state.update { it.copy(host = host, hostError = null) }
    fun updatePort(port: String) = _state.update { it.copy(port = port, portError = null) }
    fun updateScheme(scheme: RouterScheme) = _state.update {
        it.copy(
            scheme = scheme,
            httpRiskAccepted = scheme == RouterScheme.HTTPS || it.httpRiskAccepted
        )
    }
    fun updateEndpoint(endpoint: String) = _state.update { it.copy(endpoint = endpoint) }
    fun updateUsername(username: String) = _state.update { it.copy(username = username, usernameError = null) }
    fun updatePassword(password: String) = _state.update { it.copy(password = password, passwordError = null) }
    fun acceptHttpRisk() = _state.update { it.copy(httpRiskAccepted = true) }

    fun validate(): Boolean {
        val s = _state.value
        var valid = true
        var nameError: String? = null
        var hostError: String? = null
        var portError: String? = null
        var usernameError: String? = null
        var passwordError: String? = null
        if (s.name.isBlank()) { nameError = "Name is required"; valid = false }
        if (s.host.isBlank()) { hostError = "Host is required"; valid = false }
        val portNum = s.port.toIntOrNull()
        if (portNum == null || portNum < 1 || portNum > 65535) { portError = "Port must be 1-65535"; valid = false }
        if (s.username.isBlank()) { usernameError = "Username is required"; valid = false }
        if (s.password.isBlank()) { passwordError = "Password is required"; valid = false }
        _state.update { it.copy(nameError = nameError, hostError = hostError, portError = portError, usernameError = usernameError, passwordError = passwordError) }
        return valid
    }

    fun save(routerId: String? = null) {
        if (!validate()) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val s = _state.value
            val router = Router(
                id = routerId ?: "",
                name = s.name,
                host = s.host,
                port = s.port.toIntOrNull() ?: 80,
                scheme = s.scheme,
                endpoint = s.endpoint.ifBlank { null },
                username = s.username,
                passwordSecretRef = "",
                certificateFingerprint = null,
                lastSuccessAt = null,
                lastFailureReason = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            if (routerId != null) {
                routerRepository.updateRouter(
                    router,
                    if (s.password.isNotBlank()) s.password else null
                )
            } else {
                routerRepository.addRouter(router, s.password)
            }
            _state.update { it.copy(isSaving = false) }
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _state.update { it.copy(connectionState = RouterConnectionState.CHECKING) }
            val s = _state.value
            val router = Router(
                id = "probe",
                name = s.name,
                host = s.host,
                port = s.port.toIntOrNull() ?: 80,
                scheme = s.scheme,
                endpoint = s.endpoint.ifBlank { null },
                username = s.username,
                passwordSecretRef = "",
                certificateFingerprint = null,
                lastSuccessAt = null,
                lastFailureReason = null,
                createdAt = 0,
                updatedAt = 0
            )
            val result = routerRepository.testConnection("probe")
            _state.update {
                it.copy(
                    probeResult = (result as? AppResult.Success)?.data,
                    connectionState = when {
                        result is AppResult.Success && result.data.layer3Auth is Layer3Result.Success -> RouterConnectionState.ONLINE
                        result is AppResult.Success && result.data.layer3Auth is Layer3Result.AuthFailed -> RouterConnectionState.AUTH_FAILED
                        result is AppResult.Success && result.data.layer2Endpoints.any { it.value is Layer2Result.Available } -> RouterConnectionState.AUTH_REQUIRED
                        result is AppResult.Success -> RouterConnectionState.ENDPOINT_UNAVAILABLE
                        else -> RouterConnectionState.OFFLINE
                    }
                )
            }
        }
    }
}
