package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.PhoneDto
import ru.practicum.android.diploma.domain.models.Phone

class PhoneApiConverter : ApiConverter<PhoneDto, Phone> {
    override fun map(dto: PhoneDto): Phone {
        return Phone(
            comment = dto.comment,
            formatted = dto.formatted
        )
    }
}
