package ru.practicum.android.diploma.data

import android.util.Log
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
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsRequestDto
import ru.practicum.android.diploma.domain.models.*

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

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?> {
        Log.d("VacanciesRepo", "getVacancyDetails called for id: $vacancyId")
        return appDatabase
            .vacancyDetailDao()
            .getVacancyWithDetails(vacancyId)
            .map { vacancyWithDetails ->
                vacancyWithDetails?.let {
                    Log.d("VacanciesRepo", "Found in DB, converting...")
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
        Log.d("VacanciesRepo", "getVacancyDetailsFromApi called for id: $vacancyId")
        val response = networkClient.doRequest(VacancyDetailsRequestDto(vacancyId))
        Log.d("VacanciesRepo", "Response resultCode: ${response.resultCode}")
        return when (response.resultCode) {
            Response.STATUS_NETWORK_ERROR -> {
                Log.e("VacanciesRepo", "Network error")
                ApiResponse.NoInternet("Проверьте подключение к интернету", -1)
            }
            Response.SUCCESS_RESPONSE_CODE -> {
                val dto = (response as Response.VacancyDetailsResponse).body
                Log.d("VacanciesRepo", "DTO received: $dto")
                if (dto != null) {
                    try {
                        val vacancyDetails = VacancyDetails(
                            id = dto.id,
                            name = dto.name,
                            description = dto.description,
                            salary = dto.salary?.let { Salary(it.from, it.to, it.currency) },
                            address = dto.address?.let { Address(it.id, it.city, it.street, it.building, it.raw) },
                            experience = dto.experience?.let { Experience(it.id, it.name) },
                            schedule = dto.schedule?.let { Schedule(it.id, it.name) },
                            employment = dto.employment?.let { Employment(it.id, it.name) },
                            contacts = dto.contacts?.let {
                                Contacts(
                                    id = it.id,
                                    name = it.name,
                                    email = it.email,
                                    phones = it.phones.map { phone -> Phone(phone.comment, phone.formatted) }
                                )
                            },
                            employer = Employer(dto.employer.id, dto.employer.name, dto.employer.logo),
                            area = Area(dto.area.id, dto.area.name, dto.area.parentId, dto.area.areas),
                            skills = dto.skills,
                            url = dto.url,
                            industry = Industry(dto.industry.id, dto.industry.name)
                        )
                        Log.d("VacanciesRepo", "Successfully converted to VacancyDetails: ${vacancyDetails.name}")
                        ApiResponse.Success(vacancyDetails)
                    } catch (e: Exception) {
                        Log.e("VacanciesRepo", "Error parsing DTO", e)
                        ApiResponse.Error("Ошибка парсинга данных", 500)
                    }
                } else {
                    Log.e("VacanciesRepo", "DTO is null")
                    ApiResponse.Error("Данные вакансии не найдены", 404)
                }
            }
            else -> {
                Log.e("VacanciesRepo", "Unexpected error code: ${response.resultCode}")
                ApiResponse.Error("Ошибка загрузки: ${response.resultCode}", response.resultCode)
            }
        }
    }
}
