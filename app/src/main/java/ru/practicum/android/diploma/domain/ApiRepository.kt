package ru.practicum.android.diploma.domain

interface ApiRepository<T> {

    suspend fun getAllFromApi(): List<T>?
}
