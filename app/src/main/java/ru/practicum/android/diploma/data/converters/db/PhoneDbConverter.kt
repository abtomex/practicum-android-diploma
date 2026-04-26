package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.domain.models.Phone

class PhoneDbConverter {
    fun phoneToEntity(phone: Phone, contactsId: Long = 0): PhoneEntity =
        PhoneEntity(
            id = 0,
            contactsId = contactsId,
            comment = phone.comment,
            formatted = phone.formatted
        )

    fun entityToPhone(phoneEntity: PhoneEntity): Phone =
        Phone(
            comment = phoneEntity.comment,
            formatted = phoneEntity.formatted
        )
}
