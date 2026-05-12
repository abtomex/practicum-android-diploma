package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.practicum.android.diploma.presentation.viewmodel.state.AppliedFilters

class FiltersScreenViewModel : ViewModel() {

    // Состояние полей фильтрации
    private val _salaryInput = MutableStateFlow("")
    val salaryInput: StateFlow<String> = _salaryInput.asStateFlow()

    private val _selectedIndustryId = MutableStateFlow(0)
    val selectedIndustryId: StateFlow<Int> = _selectedIndustryId.asStateFlow()

    private val _selectedIndustryName = MutableStateFlow("")
    val selectedIndustryName: StateFlow<String> = _selectedIndustryName.asStateFlow()

    private val _hideWithoutSalary = MutableStateFlow(false)
    val hideWithoutSalary: StateFlow<Boolean> = _hideWithoutSalary.asStateFlow()

    // Флаг наличия активных фильтров
    val hasActiveFilters: StateFlow<Boolean> = combine(
        _salaryInput,
        _selectedIndustryId,
        _hideWithoutSalary
    ) { salary, industryId, hide ->
        salary.isNotBlank() || industryId != 0 || hide
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_DURATION),
        initialValue = false
    )

    private val _filtersApplied = MutableStateFlow<AppliedFilters?>(null)
    val filtersApplied: StateFlow<AppliedFilters?> = _filtersApplied.asStateFlow()

    fun updateSalaryInput(salary: String) {
        _salaryInput.value = salary
    }

    fun updateSelectedIndustry(industryId: Int, industryName: String) {
        _selectedIndustryId.value = industryId
        _selectedIndustryName.value = industryName
    }

    fun updateHideWithoutSalary(hide: Boolean) {
        _hideWithoutSalary.value = hide
    }

    fun resetFilters() {
        _salaryInput.value = ""
        _selectedIndustryId.value = 0
        _selectedIndustryName.value = ""
        _hideWithoutSalary.value = false
        _filtersApplied.value = AppliedFilters()
    }

    fun resetIndustry() {
        _selectedIndustryId.value = 0
        _selectedIndustryName.value = ""
        applyFilters()
    }

    fun applyFilters() {
        _filtersApplied.value = AppliedFilters(
            salary = _salaryInput.value.toIntOrNull(),
            industry = _selectedIndustryId.value,
            onlyWithSalary = _hideWithoutSalary.value
        )
    }
    companion object {
        private const val SUBSCRIPTION_DURATION = 5000L
    }
}
