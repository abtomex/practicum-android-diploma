package ru.practicum.android.diploma.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity

data class ContactsWithPhones(
    @Embedded
    val contacts: ContactsEntity,

    @Relation(parentColumn = "id", entityColumn = "contactsId")
    val phones: List<PhoneEntity>
)
