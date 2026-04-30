package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.ContactsDto
import ru.practicum.android.diploma.domain.models.Contacts

class ContactsApiConverter(val phoneConverter: PhoneApiConverter) : ApiConverter<ContactsDto?, Contacts?> {
    override fun map(dto: ContactsDto?): Contacts? {
        if (dto == null) return null
        return Contacts(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            phones = dto.phones.map { phoneConverter.map(it) }
        )
    }
}
