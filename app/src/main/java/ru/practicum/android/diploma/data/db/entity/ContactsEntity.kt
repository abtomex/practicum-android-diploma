package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    foreignKeys = [ForeignKey(
        entity = VacancyDetailsEntity::class,
        parentColumns = ["id"],
        childColumns = ["vacancyId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val vacancyId: String,
    val name: String,
    val email: String
)
