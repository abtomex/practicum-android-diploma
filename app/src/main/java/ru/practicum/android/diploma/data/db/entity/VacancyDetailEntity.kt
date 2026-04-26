package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_detail")
data class VacancyDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String, // HTML
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val skillsList: List<String>,
    val area: String,
    val url: String,
    val industry: String
)
