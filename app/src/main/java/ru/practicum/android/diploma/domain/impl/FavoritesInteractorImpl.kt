package ru.practicum.android.diploma.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.FavoritesInteractor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavoritesInteractorImpl(
    private val repository: VacanciesRepository
) : FavoritesInteractor {
    override fun getAllFavorites(): Flow<List<VacancyCard>> {
        return repository.getAllVacancyCards()
    }

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails?> {
        Log.d("FavoritesInteractor", "getVacancyDetails called for id: $vacancyId")
        return repository.getVacancyDetails(vacancyId)
    }

    override suspend fun addToFavorites(vacancy: VacancyDetails) {
        repository.addVacancyToFavorites(vacancy)
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        repository.removeVacancyFromFavorites(vacancyId)
    }
}
