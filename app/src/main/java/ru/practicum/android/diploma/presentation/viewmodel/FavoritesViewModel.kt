package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FavoritesInteractor
import ru.practicum.android.diploma.domain.models.VacancyCard

sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val vacancies: List<VacancyCard>) : FavoritesState()
    data class Error(val message: String) : FavoritesState()
    object Empty : FavoritesState()
}

class FavoritesViewModel(
    private val interactor: FavoritesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _state.value = FavoritesState.Loading
            interactor.getAllFavorites().collect { vacancies ->
                if (vacancies.isEmpty()) {
                    _state.value = FavoritesState.Empty
                } else {
                    _state.value = FavoritesState.Success(vacancies)
                }
            }
        }
    }

    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            interactor.removeFromFavorites(vacancyId)
            loadFavorites()
        }
    }
}
