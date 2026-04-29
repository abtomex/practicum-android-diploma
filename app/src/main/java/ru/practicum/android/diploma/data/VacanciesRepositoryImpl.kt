package ru.practicum.android.diploma.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.VacancyCardApiConverter
import ru.practicum.android.diploma.data.converters.db.VacancyCardDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyDetailsDbConverter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: VacancyCardApiConverter,
    val appDatabase: AppDatabase,
    val vacancyCardDbConverter: VacancyCardDbConverter,
    val vacancyDetailsDbConverter: VacancyDetailsDbConverter
) : VacanciesRepository {

    override suspend fun getAllFromApi(): List<VacancyCard>? {
        return (networkClient.doRequest(VacanciesRequestDto()) as Response.VacanciesResponse)
            .body?.items?.map { vacancyCardDto -> apiConverter.map(vacancyCardDto) }
    }

    override suspend fun searchVacancies(request: VacanciesRequestDto): ApiResponse<VacanciesDto?> {
        val response = networkClient.doRequest(request)
        return when (response.resultCode) {
            Response.STATUS_NETWORK_ERROR -> ApiResponse.NoInternet("Проверьте подключение к интернету", -1)
            Response.SUCCESS_RESPONSE_CODE -> {
                ApiResponse.Success(
                    (response as Response.VacanciesResponse).body
                )
            }

            else -> ApiResponse.Error("response result code is ${response.resultCode}", response.resultCode)
        }
    }

    override suspend fun findVacanciesByStr(strQuery: String): ApiResponse<out List<VacancyCard>?> {
        val response = networkClient.doRequest(VacanciesRequestDto(text = strQuery))
        return when (response.resultCode) {
            Response.STATUS_NETWORK_ERROR -> ApiResponse.NoInternet("Проверьте подключение к интернету", -1)
            Response.SUCCESS_RESPONSE_CODE -> {
                ApiResponse.Success(
                    (response as Response.VacanciesResponse).body?.
                        items?.map { vacancyCardDto -> apiConverter.map(vacancyCardDto) }
                )
            }

            else -> ApiResponse.Error("response result code is ${response.resultCode}", response.resultCode)
        }
    }

    override fun getAllVacancyCards(): Flow<List<VacancyCard>> =
        appDatabase
            .vacancyCardDao()
            .getVacancyCards()
            .map { entities ->
                entities.map {
                    vacancyCardDbConverter.entityToVacancyCard(it)
                }
            }

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?> =
        appDatabase
            .vacancyDetailDao()
            .getVacancyWithDetails(vacancyId)
            .filterNotNull()
            .map {
                vacancyDetailsDbConverter.fullEntityToVacancyDetails(it)
            }

    override suspend fun addVacancyToFavorites(vacancy: VacancyDetails) {
        val newVacancyCard = vacancyCardDbConverter.vacancyDetailsToVacancyCard(vacancy)

        val vacancyCardEntity = vacancyCardDbConverter.vacancyCardToEntity(newVacancyCard)
        val vacancyDetailsEntity = vacancyDetailsDbConverter.vacancyDetailsToFullEntity(vacancy)

        appDatabase.withTransaction {
            appDatabase.vacancyCardDao().insertVacancyCard(vacancyCardEntity)
            appDatabase.vacancyDetailDao().insertFullVacancy(vacancyDetailsEntity)
        }
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: String) {
        appDatabase.withTransaction {
            appDatabase.vacancyCardDao().deleteVacancyCard(vacancyId)
            appDatabase.vacancyDetailDao().deleteVacancy(vacancyId)
        }
    }

}
