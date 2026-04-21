package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.industries.IndustryDto
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesApiConverter : ApiConverter<IndustryDto, Industry> {
    override fun map(dto: IndustryDto): Industry {
        return Industry(
            id = dto.id,
            name = dto.name
        )
    }
}
