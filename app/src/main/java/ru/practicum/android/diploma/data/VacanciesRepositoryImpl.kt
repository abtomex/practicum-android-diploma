package ru.practicum.android.diploma.data

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.api.VacancyDetailsApiConverter
import ru.practicum.android.diploma.data.converters.db.VacancyCardDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyDetailsDbConverter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsRequestDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesRepositoryImpl(
    val networkClient: NetworkClient,
    val appDatabase: AppDatabase,
    val vacancyCardDbConverter: VacancyCardDbConverter,
    val vacancyDetailsDbConverter: VacancyDetailsDbConverter,
    val vacancyDetailsApiConverter: VacancyDetailsApiConverter
) : VacanciesRepository {

    override suspend fun searchVacancies(request: VacanciesRequestDto): ApiResponse<VacanciesDto?> {
        val response = networkClient.doRequest(request)
        return when (response.resultCode) {
            Response.STATUS_NETWORK_ERROR -> ApiResponse.NoInternet(
                Response.NO_INTERNET_MSG,
                Response.STATUS_NETWORK_ERROR
            )
            Response.SUCCESS_RESPONSE_CODE -> {
                ApiResponse.Success(
                    (response as Response.VacanciesResponse).body
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

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?> {
        return appDatabase
            .vacancyDetailDao()
            .getVacancyWithDetails(vacancyId)
            .map { vacancyWithDetails ->
                vacancyWithDetails?.let {
                    vacancyDetailsDbConverter.fullEntityToVacancyDetails(it)
                }
            }
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

    override suspend fun getVacancyDetailsFromApi(vacancyId: String): ApiResponse<VacancyDetails?> {
        val response = networkClient.doRequest(VacancyDetailsRequestDto(vacancyId))
        Log.d(LOG_TAG, "Response resultCode: ${response.resultCode}")
        return when (response.resultCode) {
            Response.STATUS_NETWORK_ERROR -> {
                Log.e(LOG_TAG, "Network error")
                ApiResponse.NoInternet("Проверьте подключение к интернету", -1)
            }

            Response.SUCCESS_RESPONSE_CODE -> {
                val dto = (response as Response.VacancyDetailsResponse).body
                Log.d(LOG_TAG, "DTO received: $dto")
                if (dto != null) {
                    try {
                        val vacancyDetails = vacancyDetailsApiConverter.map(dto)
                        Log.d(LOG_TAG, "Successfully converted to VacancyDetails: ${vacancyDetails.name}")
                        ApiResponse.Success(vacancyDetails)
                    } catch (_: Exception) {
                        ApiResponse.Error("Ошибка парсинга данных", Response.STATUS_SERVER_ERROR)
                    }
                } else {
                    Log.e(LOG_TAG, "DTO is null")
                    ApiResponse.Error("Данные вакансии не найдены", Response.NO_PAGE)
                }
            }

            else -> {
                Log.e(LOG_TAG, "Unexpected error code: ${response.resultCode}")
                ApiResponse.Error("Ошибка загрузки: ${response.resultCode}", response.resultCode)
            }
        }
    }

    companion object {
        private const val LOG_TAG = "VacanciesRepo"
    }
}
