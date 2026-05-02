package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.EmploymentDto
import ru.practicum.android.diploma.domain.models.Employment

class EmploymentApiConverter : ApiConverter<EmploymentDto?, Employment?> {
    override fun map(dto: EmploymentDto?): Employment? {
        if (dto == null) return null
        return Employment(
            id = dto.id,
            name = dto.name,
        )
    }
}
