package ru.practicum.android.diploma.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavoritesInteractor(
    private val repository: VacanciesRepository
) {
    fun getAllFavorites(): Flow<List<VacancyCard>> {
        return repository.getAllVacancyCards()
    }

    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?> {
        return repository.getVacancyDetails(vacancyId)
    }

    suspend fun addToFavorites(vacancy: VacancyDetails) {
        repository.addVacancyToFavorites(vacancy)
    }

    suspend fun removeFromFavorites(vacancyId: String) {
        repository.removeVacancyFromFavorites(vacancyId)
    }
}
