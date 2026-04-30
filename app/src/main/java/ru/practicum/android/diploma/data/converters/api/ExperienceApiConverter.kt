package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.ExperienceDto
import ru.practicum.android.diploma.domain.models.Experience

class ExperienceApiConverter : ApiConverter<ExperienceDto?, Experience?> {
    override fun map(dto: ExperienceDto?): Experience? {
        if (dto == null) return null
        return Experience(
            id = dto.id,
            name = dto.name
        )
    }
}
