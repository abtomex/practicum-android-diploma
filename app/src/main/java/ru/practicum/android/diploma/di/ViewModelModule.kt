package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.NetworkConnectivityChecker

val viewModelModule = module {
    single {
        NetworkConnectivityChecker(context = get())
    }
    viewModel { FavoritesViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
}
