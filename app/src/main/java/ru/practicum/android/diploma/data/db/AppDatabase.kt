package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.data.converters.StringListConverter
import ru.practicum.android.diploma.data.db.dao.VacancyCardDao
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

@Database(
    entities = [
        AddressEntity::class,
        EmployerEntity::class,
        PhoneEntity::class,
        SalaryEntity::class,
        VacancyCardEntity::class,
        VacancyDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacancyDetailDao(): VacancyDetailDao
    abstract fun vacancyCardDao(): VacancyCardDao
}
