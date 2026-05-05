package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.practicum.android.diploma.data.AreasRepositoryImpl
import ru.practicum.android.diploma.data.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.converters.api.AddressApiConverter
import ru.practicum.android.diploma.data.converters.api.AreaApiConverter
import ru.practicum.android.diploma.data.converters.api.ContactsApiConverter
import ru.practicum.android.diploma.data.converters.api.EmployerApiConverter
import ru.practicum.android.diploma.data.converters.api.EmploymentApiConverter
import ru.practicum.android.diploma.data.converters.api.ExperienceApiConverter
import ru.practicum.android.diploma.data.converters.api.IndustryApiConverter
import ru.practicum.android.diploma.data.converters.api.PhoneApiConverter
import ru.practicum.android.diploma.data.converters.api.SalaryApiConverter
import ru.practicum.android.diploma.data.converters.api.ScheduleApiConverter
import ru.practicum.android.diploma.data.converters.api.VacanciesApiConverter
import ru.practicum.android.diploma.data.converters.api.VacancyCardApiConverter
import ru.practicum.android.diploma.data.converters.api.VacancyDetailsApiConverter
import ru.practicum.android.diploma.data.converters.api.VacancyRequestApiConverter
import ru.practicum.android.diploma.domain.AreasRepository
import ru.practicum.android.diploma.domain.IndustriesRepository
import ru.practicum.android.diploma.domain.VacanciesRepository

val repositoryModule = module {

    factory { Gson() }

    factory { VacancyCardApiConverter() }
    factory { VacanciesApiConverter(get()) }
    factory { VacancyRequestApiConverter() }

    factory { AddressApiConverter() }
    factory { AreaApiConverter() }
    factory { ContactsApiConverter(get()) }
    factory { EmployerApiConverter() }
    factory { EmploymentApiConverter() }
    factory { ExperienceApiConverter() }
    factory { IndustryApiConverter() }
    factory { PhoneApiConverter() }
    factory { SalaryApiConverter() }
    factory { ScheduleApiConverter() }
    factory { VacancyCardApiConverter() }
    factory { VacancyDetailsApiConverter() }

    single<AreasRepository> {
        AreasRepositoryImpl(get(), get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(get(), get())
    }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(get(), get(), get(), get(), get(), get())
    }

}
