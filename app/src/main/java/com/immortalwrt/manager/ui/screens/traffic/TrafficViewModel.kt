package com.immortalwrt.manager.ui.screens.traffic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immortalwrt.manager.core.network.UbusJsonRpcClient
import com.immortalwrt.manager.core.session.SessionManager
import com.immortalwrt.manager.domain.model.*
import com.immortalwrt.manager.domain.repository.NetworkRepository
import com.immortalwrt.manager.domain.repository.RouterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class TrafficUiState(
    val interfaces: List<NetworkInterface> = emptyList(),
    val selectedInterface: String = "",
    val dataPoints: List<TrafficDataPoint> = emptyList(),
    val isPolling: Boolean = false,
    val uiState: LoadableUiState<Unit> = LoadableUiState.Idle
)

data class TrafficDataPoint(
    val time: Long,
    val rxBytes: Long,
    val txBytes: Long,
    val rxRate: Float = 0f,
    val txRate: Float = 0f
)

@HiltViewModel
class TrafficViewModel @Inject constructor(
    private val rpcClient: UbusJsonRpcClient,
    private val sessionManager: SessionManager,
    private val routerRepository: RouterRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TrafficUiState())
    val state: StateFlow<TrafficUiState> = _state.asStateFlow()
    private var pollingJob: Job? = null
    private var lastDataPoint: TrafficDataPoint? = null

    fun loadInterfaces(routerId: String) {
        viewModelScope.launch {
            val ifaces = networkRepository.getCachedInterfaces(routerId)
            _state.update { it.copy(interfaces = ifaces, selectedInterface = ifaces.firstOrNull()?.logicalName ?: "") }
        }
    }

    fun selectInterface(ifname: String) {
        _state.update { it.copy(selectedInterface = ifname, dataPoints = emptyList()) }
        lastDataPoint = null
    }

    fun startPolling(routerId: String, ifname: String) {
        pollingJob?.cancel()
        _state.update { it.copy(isPolling = true) }
        pollingJob = viewModelScope.launch {
            val router = routerRepository.getById(routerId) ?: return@launch
            val sessionResult = sessionManager.getSession(routerId)
            if (sessionResult !is AppResult.Success) return@launch
            val session = sessionResult.data
            val endpoint = router.endpoint ?: "/ubus"
            val securityContext = RouterSecurityContext(
                routerId = routerId, scheme = router.scheme, host = router.host,
                port = router.port, certificateFingerprint = router.certificateFingerprint,
                httpRiskAccepted = router.scheme == RouterScheme.HTTP
            )

            while (isActive) {
                val result = rpcClient.call(
                    securityContext, session.sessionId, endpoint,
                    "luci", "getRealtimeStats",
                    mapOf("mode" to "interface", "device" to ifname)
                )
                if (result is AppResult.Success) {
                    val rawData = result.data as? List<*> ?: emptyList<Any>()
                    if (rawData.isNotEmpty()) {
                        val lastEntry = rawData.lastOrNull() as? List<*> ?: emptyList<Any>()
                        if (lastEntry.size >= 5) {
                            val time = (lastEntry[0] as? Number)?.toLong() ?: 0L
                            val rxBytes = (lastEntry[1] as? Number)?.toLong() ?: 0L
                            val txBytes = (lastEntry[3] as? Number)?.toLong() ?: 0L
                            val point = TrafficDataPoint(time = time, rxBytes = rxBytes, txBytes = txBytes)
                            val previous = lastDataPoint
                            if (previous != null && previous.rxBytes > 0 && point.time != previous.time) {
                                val dt = point.time - previous.time
                                val newPoint = point.copy(
                                    rxRate = ((point.rxBytes - previous.rxBytes).toFloat() / dt).coerceAtLeast(0f),
                                    txRate = ((point.txBytes - previous.txBytes).toFloat() / dt).coerceAtLeast(0f)
                                )
                                _state.update {
                                    val points = it.dataPoints.toMutableList()
                                    points.add(newPoint)
                                    if (points.size > 60) points.removeAt(0)
                                    it.copy(dataPoints = points)
                                }
                            }
                            lastDataPoint = point
                        }
                    }
                    _state.update { it.copy(uiState = LoadableUiState.Content(Unit)) }
                }
                delay(3000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
        _state.update { it.copy(isPolling = false) }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}
