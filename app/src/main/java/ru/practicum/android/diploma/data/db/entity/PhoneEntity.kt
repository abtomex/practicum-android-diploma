package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Contacts: один-ко-многим
@Entity(tableName = "phones")
data class PhoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val contactsId: String,
    val comment: String?,
    val formatted: String
)
