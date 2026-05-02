package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.usecases.AreasInteractor

class FiltersViewModel(
    private val areaInteractor: AreasInteractor
) : ViewModel() {
    val listAreas = MutableStateFlow<List<Area>>(emptyList())
    val areaFilter = MutableStateFlow<Area?>(null)

    init {
        getAreasList()
    }

    fun getAreasList() {
        viewModelScope.launch {
            listAreas.value = (areaInteractor.getAreasList() ?: emptyList())
        }

    }

    fun setFilter(area: Area) {
        areaFilter.value = area
    }

}
