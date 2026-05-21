package com.immortalwrt.manager.ui.screens.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.core.security.SecretStore
import com.immortalwrt.manager.core.session.SessionManager
import com.immortalwrt.manager.domain.model.*
import com.immortalwrt.manager.domain.repository.RouterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PasswordChangeUiState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isChanging: Boolean = false,
    val isConfirming: Boolean = false,
    val result: PasswordChangeResult? = null,
    val routerName: String = ""
)

sealed interface PasswordChangeResult {
    data object Success : PasswordChangeResult
    data class Failed(val message: String) : PasswordChangeResult
    data class ReLoginRequired(val message: String) : PasswordChangeResult
    data object Unsupported : PasswordChangeResult
}

@HiltViewModel
class PasswordChangeViewModel @Inject constructor(
    private val rpcClient: UbusJsonRpcClient,
    private val sessionManager: SessionManager,
    private val secretStore: SecretStore,
    private val routerRepository: RouterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PasswordChangeUiState())
    val state: StateFlow<PasswordChangeUiState> = _state.asStateFlow()

    fun updateNewPassword(pw: String) = _state.update { it.copy(newPassword = pw, result = null) }
    fun updateConfirmPassword(pw: String) = _state.update { it.copy(confirmPassword = pw, result = null) }

    fun changePassword(routerId: String) {
        val s = _state.value
        if (s.newPassword != s.confirmPassword) {
            _state.update { it.copy(result = PasswordChangeResult.Failed("Passwords do not match")) }
            return
        }
        if (s.newPassword.length < 1) {
            _state.update { it.copy(result = PasswordChangeResult.Failed("Password cannot be empty")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isChanging = true) }
            val router = routerRepository.getById(routerId) ?: run {
                _state.update { it.copy(isChanging = false, result = PasswordChangeResult.Failed("Router not found")) }
                return@launch
            }
            val sessionResult = sessionManager.getSession(routerId)
            if (sessionResult !is AppResult.Success) {
                _state.update { it.copy(isChanging = false, result = PasswordChangeResult.Failed("Not authenticated")) }
                return@launch
            }
            val session = sessionResult.data
            val endpoint = router.endpoint ?: "/ubus"
            val securityContext = RouterSecurityContext(
                routerId = routerId, scheme = router.scheme, host = router.host,
                port = router.port, certificateFingerprint = router.certificateFingerprint,
                httpRiskAccepted = router.scheme == RouterScheme.HTTP
            )

            // Step 1: Call luci.setPassword
            val result = rpcClient.call(
                securityContext, session.sessionId, endpoint,
                "luci", "setPassword",
                mapOf("username" to router.username, "password" to s.newPassword)
            )

            when {
                result is AppResult.Success -> {
                    // Step 2: Clear current session
                    sessionManager.invalidate(routerId)

                    // Step 3: Try re-login with new password
                    val loginResult = sessionManager.login(routerId, securityContext, endpoint, router.username, s.newPassword)
                    when (loginResult) {
                        is AppResult.Success -> {
                            // Step 4: Update stored password
                            val alias = "router_${routerId}_password"
                            secretStore.delete(alias)
                            secretStore.store(alias, s.newPassword.toByteArray())
                            _state.update { it.copy(isChanging = false, result = PasswordChangeResult.Success) }
                        }
                        is AppResult.Failure -> {
                            _state.update { it.copy(isChanging = false, result = PasswordChangeResult.ReLoginRequired("Password may have changed. Please log in with the new password.")) }
                        }
                    }
                }
                result is AppResult.Failure && result.error is AppError.UnsupportedCapability -> {
                    _state.update { it.copy(isChanging = false, result = PasswordChangeResult.Unsupported) }
                }
                result is AppResult.Failure -> {
                    val msg = (result.error as? AppError.UbusError)?.message ?: "Failed to change password"
                    _state.update { it.copy(isChanging = false, result = PasswordChangeResult.Failed(msg)) }
                }
            }
        }
    }

    fun load(routerId: String) {
        viewModelScope.launch {
            val router = routerRepository.getById(routerId)
            _state.update { it.copy(routerName = router?.name ?: "") }
        }
    }
}
