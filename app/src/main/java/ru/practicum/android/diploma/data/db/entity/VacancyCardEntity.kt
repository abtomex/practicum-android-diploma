package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_card")
data class VacancyCardEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val company: String,
    val logo: String,
    val city: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?
)
