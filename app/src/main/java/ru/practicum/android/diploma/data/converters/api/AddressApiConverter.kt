package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.AddressDto
import ru.practicum.android.diploma.domain.models.Address

class AddressApiConverter : ApiConverter<AddressDto?, Address?> {
    override fun map(dto: AddressDto?): Address? {
        if (dto == null) return null
        return Address(
            id = dto.id,
            city = dto.city,
            street = dto.street,
            building = dto.building,
            raw = dto.raw
        )
    }
}
