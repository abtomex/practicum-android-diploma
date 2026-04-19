package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employment")
data class EmploymentEntity(
    @PrimaryKey
    val id: String,
    val name: String
)
