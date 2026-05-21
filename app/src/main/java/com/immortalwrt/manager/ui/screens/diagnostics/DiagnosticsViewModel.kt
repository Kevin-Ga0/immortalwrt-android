package com.immortalwrt.manager.ui.screens.diagnostics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.domain.model.*
import com.immortalwrt.manager.domain.repository.CapabilityRepository
import com.immortalwrt.manager.domain.repository.RouterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiagnosticsUiState(
    val router: Router? = null,
    val environment: RouterEnvironment? = null,
    val capability: RouterCapability? = null,
    val connectionState: RouterConnectionState = RouterConnectionState.UNKNOWN,
    val macAnonymized: Boolean = false,
    val ipAnonymized: Boolean = false,
    val hostnameHidden: Boolean = false,
    val reportContent: String = ""
)

@HiltViewModel
class DiagnosticsViewModel @Inject constructor(
    private val routerRepository: RouterRepository,
    private val capabilityRepository: CapabilityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DiagnosticsUiState())
    val state: StateFlow<DiagnosticsUiState> = _state.asStateFlow()

    fun load(routerId: String) {
        viewModelScope.launch {
            val router = routerRepository.getById(routerId)
            val env = capabilityRepository.getEnvironment(routerId)
            val cap = capabilityRepository.getCapability(routerId)
            _state.update {
                it.copy(router = router, environment = env, capability = cap)
            }
        }
    }

    fun toggleMacAnon() = _state.update { it.copy(macAnonymized = !it.macAnonymized) }
    fun toggleIpAnon() = _state.update { it.copy(ipAnonymized = !it.ipAnonymized) }
    fun toggleHostnameHidden() = _state.update { it.copy(hostnameHidden = !it.hostnameHidden) }

    fun generateReport() {
        val s = _state.value
        val sb = StringBuilder()
        sb.appendLine("# Diagnostic Report")
        sb.appendLine("**Router**: ${s.router?.name} (${s.router?.host}:${s.router?.port})")
        sb.appendLine("**Protocol**: ${s.router?.scheme?.name}")
        sb.appendLine("**Environment**: distribution=${s.environment?.distribution ?: "N/A"}, kernel=${s.environment?.kernelVersion ?: "N/A"}")
        sb.appendLine("**Endpoint Mode**: ${s.environment?.endpointMode?.name ?: "N/A"}")
        sb.appendLine("**LuCI**: ${s.environment?.luciAvailable}, **rpcd**: ${s.environment?.rpcdAvailable}")
        sb.appendLine()
        sb.appendLine("## Capabilities")
        s.capability?.let { cap ->
            listOf(
                "session.login" to cap.hasSessionLogin, "system.board" to cap.hasSystemBoard,
                "system.info" to cap.hasSystemInfo, "network.interface.dump" to cap.hasNetworkInterfaceDump,
                "host hints" to cap.hasHostHints, "DHCP leases" to cap.hasDhcpLeases,
                "realtime stats" to cap.hasRealtimeStats, "temperature" to cap.hasTempInfo,
                "reboot" to cap.canReboot, "change password" to cap.canChangePassword
            ).forEach { (name, available) -> sb.appendLine("- $name: ${if (available) "OK" else "N/A"}") }
        }
        sb.appendLine()
        sb.appendLine("## Anonymization")
        sb.appendLine("- IPs: ${if (s.ipAnonymized) "anonymized" else "raw"}")
        sb.appendLine("- MACs: ${if (s.macAnonymized) "anonymized" else "raw"}")
        sb.appendLine("- Hostnames: ${if (s.hostnameHidden) "hidden" else "visible"}")
        _state.update { it.copy(reportContent = sb.toString()) }
    }
}
