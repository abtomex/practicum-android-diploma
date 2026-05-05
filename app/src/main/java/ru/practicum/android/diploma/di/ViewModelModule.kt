package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.presentation.viewmodel.FiltersScreenViewModel
import ru.practicum.android.diploma.presentation.viewmodel.IndustryFiltersViewModel
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

val viewModelModule = module {
    viewModel { FavoritesViewModel(get()) }
    viewModel { IndustryFiltersViewModel(get()) }
    viewModel { FiltersScreenViewModel() }
    viewModel { SearchViewModel(get()) }
    viewModel { VacancyDetailsViewModel(get(), get()) }
}
