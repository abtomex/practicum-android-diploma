package ru.practicum.android.diploma.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.VacancyCardApiConverter
import ru.practicum.android.diploma.data.converters.db.VacancyCardDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyDetailsDbConverter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancies.VacancyResponseDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails
import kotlin.collections.map

class VacanciesRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: VacancyCardApiConverter,
    val appDatabase: AppDatabase,
    val vacancyCardDbConverter: VacancyCardDbConverter,
    val vacancyDetailsDbConverter: VacancyDetailsDbConverter
) : VacanciesRepository {

    override suspend fun getAllFromApi(): List<VacancyCard> {
        return (networkClient.doRequest(VacanciesRequestDto()) as VacancyResponseDto)
            .items.map { vacancyCardDto -> apiConverter.map(vacancyCardDto) }
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
