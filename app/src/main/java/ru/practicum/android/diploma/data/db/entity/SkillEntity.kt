package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// VacancyDetail: один-ко-многим
@Entity(tableName = "skill")
data class SkillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val vacancyId: String,
    val skill: String
)
