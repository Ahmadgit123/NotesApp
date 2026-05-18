package com.ahmadziya.notesapp.network.model

// Sealed class = only these 3 states are possible
sealed class UiState<out T> {
    object Loading                            : UiState<Nothing>()
    data class Success<T>(val data: T)        : UiState<T>()
    data class Error(val message: String)     : UiState<Nothing>()
}