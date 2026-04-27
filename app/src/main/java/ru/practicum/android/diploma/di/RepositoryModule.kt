package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.practicum.android.diploma.data.AreasRepositoryImpl
import ru.practicum.android.diploma.data.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.converters.AreasApiConverter
import ru.practicum.android.diploma.data.converters.IndustriesApiConverter
import ru.practicum.android.diploma.data.converters.VacancyCardApiConverter
import ru.practicum.android.diploma.domain.AreasRepository
import ru.practicum.android.diploma.domain.IndustriesRepository
import ru.practicum.android.diploma.domain.VacanciesRepository

val repositoryModule = module {

    factory { Gson() }

    factory { AreasApiConverter() }
    factory { IndustriesApiConverter() }
    factory { VacancyCardApiConverter() }

    single<AreasRepository> {
        AreasRepositoryImpl(get(), get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(get(), get())
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(get(), get(), get(), get(), get())
    }

}
