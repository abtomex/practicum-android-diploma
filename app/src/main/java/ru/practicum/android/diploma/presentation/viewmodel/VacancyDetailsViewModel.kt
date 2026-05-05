package ru.practicum.android.diploma.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.usecases.FavoritesInteractor
import ru.practicum.android.diploma.domain.usecases.GetVacancyDetailsUseCase

private const val TAG = "VacancyDetailsVM"

sealed class VacancyDetailsState {
    data object Loading : VacancyDetailsState()
    data class Content(val vacancy: VacancyDetails) : VacancyDetailsState()
    data object Error : VacancyDetailsState()
    data object NoInternet : VacancyDetailsState()
}

class VacancyDetailsViewModel(
    private val getVacancyDetailsUseCase: GetVacancyDetailsUseCase,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyDetailsState>(VacancyDetailsState.Loading)
    val state: StateFlow<VacancyDetailsState> = _state.asStateFlow()

    fun loadVacancyDetails(vacancyId: String, fromNetwork: Boolean = true) {
        Log.d(TAG, "loadVacancyDetails CALLED with id: $vacancyId, fromNetwork: $fromNetwork")
        viewModelScope.launch {
            // Сначала проверяем в БД (избранное)
            checkFromDb(vacancyId)

            // Если из БД нет и нужна сеть
            if (fromNetwork) {
                checkFromNetwork(vacancyId)

            } else {
                Log.d(TAG, "fromNetwork=false, not fetching from network")
            }
        }
    }

    private suspend fun checkFromDb(vacancyId: String) {
        Log.d(TAG, "Checking DB for vacancy: $vacancyId")
        val dbVacancy = favoritesInteractor.getVacancyDetails(vacancyId).first()
        if (dbVacancy != null) {
            Log.d(TAG, "Found vacancy in DB: ${dbVacancy.name}")
            _state.value = VacancyDetailsState.Content(dbVacancy)
            return
        }
        Log.d(TAG, "Vacancy NOT found in DB, continuing to network...")

    }

    private suspend fun checkFromNetwork(vacancyId: String) {
        Log.d(TAG, "Fetching from network for id: $vacancyId")
        _state.value = VacancyDetailsState.Loading
        getVacancyDetailsUseCase(vacancyId)
            .catch { e ->
                Log.e(TAG, "Error fetching from network", e)
                _state.value = VacancyDetailsState.Error
            }
            .collect { response ->
                Log.d(TAG, "Network response received: $response")
                when (response) {
                    is ApiResponse.Success -> {
                        if (response.data != null) {
                            Log.d(TAG, "Success! Vacancy name: ${response.data.name}")
                            _state.value = VacancyDetailsState.Content(response.data)
                        } else {
                            Log.e(TAG, "Success but data is null")
                            _state.value = VacancyDetailsState.Error
                        }
                    }

                    is ApiResponse.NoInternet -> {
                        Log.e(TAG, "No internet")
                        _state.value = VacancyDetailsState.NoInternet
                    }

                    is ApiResponse.Error -> {
                        Log.e(TAG, "Error: ${response.message}, code: ${response.code}")
                        _state.value = VacancyDetailsState.Error
                    }
                }
            }
    }

    fun toggleFavorite(vacancy: VacancyDetails, isFavorite: Boolean) {
        Log.d(TAG, "toggleFavorite: vacancyId=${vacancy.id}, isFavorite=$isFavorite")
        viewModelScope.launch {
            if (isFavorite) {
                Log.d(TAG, "Removing from favorites: ${vacancy.id}")
                favoritesInteractor.removeFromFavorites(vacancy.id)
            } else {
                Log.d(TAG, "Adding to favorites: ${vacancy.id}")
                favoritesInteractor.addToFavorites(vacancy)
            }
        }
    }

    fun checkIsFavorite(vacancyId: String, onResult: (Boolean) -> Unit) {
        Log.d(TAG, "checkIsFavorite CALLED for id: $vacancyId")
        viewModelScope.launch {
            Log.d(TAG, "checkIsFavorite: collecting from DB")
            val details = favoritesInteractor.getVacancyDetails(vacancyId).first()
            val isFav = details != null
            Log.d(TAG, "checkIsFavorite: isFavorite=$isFav")
            onResult(isFav)
        }
    }
}
