package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Employer: один-ко-многим
@Entity(tableName = "phones")
data class PhoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val employerId: String,
    val comment: String?,
    val formatted: String
)
