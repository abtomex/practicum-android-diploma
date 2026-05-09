package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface FavoritesInteractor {

    fun getAllFavorites(): Flow<List<VacancyCard>>
    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?>
    suspend fun addToFavorites(vacancy: VacancyDetails)
    suspend fun removeFromFavorites(vacancyId: String)
}
