package ru.practicum.android.diploma.domain.usecases

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyDetails

private const val TAG = "GetVacancyDetailsUseCase"

class GetVacancyDetailsUseCase(
    private val repository: VacanciesRepository
) {
    operator fun invoke(vacancyId: String): Flow<ApiResponse<VacancyDetails?>> = flow {
        Log.d(TAG, "invoke() called with id: $vacancyId")
        val result = repository.getVacancyDetailsFromApi(vacancyId)
        Log.d(TAG, "Repository result: $result")
        emit(result)
    }.flowOn(Dispatchers.IO)
}
