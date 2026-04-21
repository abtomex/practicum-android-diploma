package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "address",
    foreignKeys = [ForeignKey(
        entity = VacancyDetailEntity::class,
        parentColumns = ["id"],
        childColumns = ["vacancyId"],
        onDelete = ForeignKey.CASCADE
    )])
data class AddressEntity(
    @PrimaryKey
    val id: String,
    val vacancyId: String,
    val city: String,
    val street: String,
    val building: String,
    val raw: String
)
