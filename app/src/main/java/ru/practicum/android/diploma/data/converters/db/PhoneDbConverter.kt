package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.domain.models.Phone

class PhoneDbConverter {
    fun phoneToEntity(phone: Phone): PhoneEntity =
        PhoneEntity(
            id = 0,
            contactsId = "",
            comment = phone.comment,
            formatted = phone.formatted
        )

    fun entityToPhone(phoneEntity: PhoneEntity): Phone =
        Phone(
            comment = phoneEntity.comment,
            formatted = phoneEntity.formatted
        )
}
