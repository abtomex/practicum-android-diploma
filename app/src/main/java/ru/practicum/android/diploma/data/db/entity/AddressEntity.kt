package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class AddressEntity(
    @PrimaryKey
    val id: String,
    val city: String,
    val street: String,
    val building: String,
    val raw: String
)
