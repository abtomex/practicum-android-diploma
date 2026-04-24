package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "salary",
    foreignKeys = [ForeignKey(
        entity = VacancyDetailsEntity::class,
        parentColumns = ["id"],
        childColumns = ["vacancyId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SalaryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val vacancyId: String,
    val from: Int?,
    val to: Int?,
    val currency: String?
)
