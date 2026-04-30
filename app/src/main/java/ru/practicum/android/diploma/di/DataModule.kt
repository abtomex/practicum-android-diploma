package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.converters.api.VacancyRequestApiConverter
import ru.practicum.android.diploma.data.converters.db.AddressDbConverter
import ru.practicum.android.diploma.data.converters.db.ContactsDbConverter
import ru.practicum.android.diploma.data.converters.db.EmployerDbConverter
import ru.practicum.android.diploma.data.converters.db.PhoneDbConverter
import ru.practicum.android.diploma.data.converters.db.SalaryDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyCardDbConverter
import ru.practicum.android.diploma.data.converters.db.VacancyDetailsDbConverter
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.AuthInterceptor
import ru.practicum.android.diploma.data.network.EndpointsApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModule = module {

    single<OkHttpClient> {
        val token: String = BuildConfig.API_ACCESS_TOKEN
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    single<EndpointsApiService> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EndpointsApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    // region Converters
    single {
        AddressDbConverter()
    }
    single {
        ContactsDbConverter(get())
    }
    single {
        EmployerDbConverter()
    }
    single {
        PhoneDbConverter()
    }
    single {
        SalaryDbConverter()
    }
    single {
        VacancyCardDbConverter()
    }
    single {
        VacancyDetailsDbConverter(get(), get(), get(), get())
    }
    single {
        VacancyRequestApiConverter()
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
