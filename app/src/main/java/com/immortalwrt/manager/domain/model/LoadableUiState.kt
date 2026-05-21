package com.immortalwrt.manager.domain.model

sealed interface LoadableUiState<out T> {
    data object Idle : LoadableUiState<Nothing>
    data object Loading : LoadableUiState<Nothing>
    data class Content<T>(val data: T, val isCached: Boolean = false) : LoadableUiState<T>
    data class Empty(val message: String) : LoadableUiState<Nothing>
    data class Error(val error: AppError, val cachedDataAvailable: Boolean = false) : LoadableUiState<Nothing>
    data class Unsupported(val capability: String) : LoadableUiState<Nothing>
}
