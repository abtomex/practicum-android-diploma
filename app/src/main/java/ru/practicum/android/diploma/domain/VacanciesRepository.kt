package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesRepository : ApiRepository<VacancyCard> {

    suspend fun searchVacancies(request: VacanciesRequestDto): ApiResponse<VacanciesDto?>
    suspend fun findVacanciesByStr(strQuery: String): ApiResponse<out List<VacancyCard>?>
    fun getAllVacancyCards(): Flow<List<VacancyCard>>
    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?>
    suspend fun addVacancyToFavorites(vacancy: VacancyDetails)
    suspend fun removeVacancyFromFavorites(vacancyId: String)
    suspend fun getVacancyDetailsFromApi(vacancyId: String): ApiResponse<VacancyDetails?>
}
