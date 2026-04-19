package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_detail")
data class VacancyDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String, // HTML
    val salaryId: String?,
    val addressId: String?,
    val experienceId: String?,
    val scheduleId: String?,
    val employmentId: String?,
    val contactsId: String?,
    val employerId: String,
    val areaId: Int,
    val url: String,
    val industryId: String
)
