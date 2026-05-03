package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.Industry

interface IndustriesInteractor {
    suspend fun getIndustriesList(): List<Industry>?
}
