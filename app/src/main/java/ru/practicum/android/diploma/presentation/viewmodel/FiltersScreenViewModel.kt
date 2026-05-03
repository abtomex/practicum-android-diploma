package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchFiltersState

class FiltersScreenViewModel(
    val industryFiltersViewModel: IndustryFiltersViewModel
) : ViewModel() {

    private val _state = MutableStateFlow<SearchFiltersState>(SearchFiltersState.Default)
    val state: StateFlow<SearchFiltersState> = _state.asStateFlow()

    // todo: подписаться на стейт industryFiltersViewModel и, если он is IndustryFiltersState.Checked, то взять для заполнения его checkedIndustry

    // todo: когда пользователь выполнил заполнение фильтров и нажал кнопку Применить state данной viewModel становится
    //    и выполняется возвращение на предыдущий экран navController.navigateUp()
    fun applyFilters(salaryInput: String, selectedIndustry: String, hideWithoutSalary: Boolean) {
        _state.value = SearchFiltersState.SpecifiedFilters(VacancyRequestByPages(
//            industry =
//            salary = salaryInput.
//            page =
//            onlyWithSalary =
        ))
    }

}
