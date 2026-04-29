package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.usecases.FavoritesInteractor

val interactorModule = module {
    single {
        FavoritesInteractor(get())
    }
    single<SearchInteractor> {
        SearchInteractorImpl(get(), get(), get())
    }
}
