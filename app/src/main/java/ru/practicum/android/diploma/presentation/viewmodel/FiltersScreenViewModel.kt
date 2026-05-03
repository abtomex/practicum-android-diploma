package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState

class FiltersScreenViewModel(
    val industryFiltersViewModel: IndustryFiltersViewModel
) : ViewModel() {
    private val _state = MutableStateFlow<SearchState>(SearchState.Default)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    // todo: подписаться на стейт industryFiltersViewModel и, если он is IndustryFiltersState.Checked, то взять для заполнения его checkedIndustry

    // todo: когда пользователь выполнил заполнение фильтров и нажал кнопку Применить state данной viewModel становится
//    и выполняется возвращение на предыдущий экран navController.navigateUp()
}
