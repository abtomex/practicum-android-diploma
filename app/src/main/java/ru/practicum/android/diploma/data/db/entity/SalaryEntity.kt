package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "salary")
data class SalaryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val from: Int?,
    val to: Int?,
    val currency: String?
)
