package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.EmployerDto
import ru.practicum.android.diploma.domain.models.Employer

class EmployerApiConverter : ApiConverter<EmployerDto, Employer> {
    override fun map(dto: EmployerDto): Employer {
        return Employer(
            id = dto.id,
            name = dto.name,
            logo = dto.logo
        )
    }
}
