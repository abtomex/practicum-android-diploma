package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.viewModelModule

class DvdJobApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DvdJobApp)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)

        }
    }
}
