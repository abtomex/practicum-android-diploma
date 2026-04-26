package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesRepository : ApiRepository<VacancyCard> {
    fun getAllVacancyCards(): Flow<List<VacancyCard>>
    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails>
    suspend fun addVacancyToFavorites(vacancy: VacancyDetails)
    suspend fun removeVacancyFromFavorites(vacancyId: String)
}
