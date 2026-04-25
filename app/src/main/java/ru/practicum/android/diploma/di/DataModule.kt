package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.converters.db.AddressDbConverter
import ru.practicum.android.diploma.data.converters.db.ContactsDbConverter
import ru.practicum.android.diploma.data.converters.db.EmployerDbConverter
import ru.practicum.android.diploma.data.converters.db.PhoneDbConverter
import ru.practicum.android.diploma.data.converters.db.SalaryDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyCardDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyDetailsDbConverter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.EndpointsApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModule = module {

    single<EndpointsApiService> {
        Retrofit.Builder()
            .baseUrl("https://android-diploma.education-services.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndpointsApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    // region Converters
    factory {
        AddressDbConverter()
    }
    factory {
        ContactsDbConverter(get())
    }
    factory {
        EmployerDbConverter()
    }
    factory {
        PhoneDbConverter()
    }
    factory {
        SalaryDbConverter()
    }
    factory {
        VacancyCardDbConverter()
    }
    factory {
        VacancyDetailsDbConverter(get(), get(), get(), get())
    }
    // endregion

    factory {
        Gson()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
