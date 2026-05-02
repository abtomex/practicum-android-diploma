package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.usecases.AreasInteractor
import ru.practicum.android.diploma.presentation.viewmodel.state.AreaFiltersState

class AreaFiltersViewModel(
    private val areaInteractor: AreasInteractor
) : ViewModel() {

    val _state = MutableStateFlow< AreaFiltersState>(AreaFiltersState.Default)
    val state: StateFlow<AreaFiltersState> = _state.asStateFlow()
    val _listAreas = MutableStateFlow<List<Area>>(emptyList())
    val listAreas: StateFlow<List<Area>> = _listAreas.asStateFlow()
    val _areaFilter = MutableStateFlow<Area?>(null)
    val areaFilter: StateFlow<Area?> = _areaFilter.asStateFlow()

    init {
        getAreasList()
    }

    fun getAreasList() {
        viewModelScope.launch {
            _listAreas.value = (areaInteractor.getAreasList() ?: emptyList())
            _state.value = AreaFiltersState.Content(
                data = _listAreas.value,
                checked = null
            )
        }

    }

    fun setFilter(area: Area) {
        _areaFilter.value = area
        _state.value = AreaFiltersState.Content(
            data = _listAreas.value,
            checked = area
        )
    }

}
