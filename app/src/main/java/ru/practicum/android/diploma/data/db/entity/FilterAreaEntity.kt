package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter_area")
data class FilterAreaEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)
