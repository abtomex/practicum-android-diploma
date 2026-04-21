package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "employer",
    foreignKeys = [ForeignKey(
        entity = VacancyDetailEntity::class,
        parentColumns = ["id"],
        childColumns = ["vacancyId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EmployerEntity(
    @PrimaryKey
    val id: String,
    val vacancyId: String,
    val name: String,
    val logo: String,
    val contactName: String?,
    val contactEmail: String?
)
