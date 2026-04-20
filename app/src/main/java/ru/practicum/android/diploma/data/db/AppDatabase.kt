package ru.practicum.android.diploma.data.db

import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {
    abstract fun vacanciesDao()
}
