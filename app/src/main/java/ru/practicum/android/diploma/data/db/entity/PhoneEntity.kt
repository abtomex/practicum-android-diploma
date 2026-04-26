package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Contacts: один-ко-многим
@Entity(
    tableName = "phones",
    foreignKeys = [ForeignKey(
        entity = ContactsEntity::class,
        parentColumns = ["id"],
        childColumns = ["contactsId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PhoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val contactsId: Long,
    val comment: String?,
    val formatted: String
)
