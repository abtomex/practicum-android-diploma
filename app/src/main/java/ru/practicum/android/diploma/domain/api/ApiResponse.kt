package ru.practicum.android.diploma.domain.api

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Error<T>(val message: String, val code: Int) : ApiResponse<T>
    data class NoInternet<T>(val message: String, val code: Int) : ApiResponse<T>
}
