package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.vacancies.VacancyCardDto
import ru.practicum.android.diploma.domain.models.VacancyCard

class VacancyCardApiConverter(
    val salaryConverter: SalaryApiConverter
) : ApiConverter<VacancyCardDto, VacancyCard> {
    override fun map(dto: VacancyCardDto?): VacancyCard? {
        if (dto == null) return null
        return VacancyCard(
            id = dto.id,
            name = dto.name,
            company = dto.company,
            city = dto.city,
            salary = salaryConverter.map(dto.salary),
            logo = dto.logo
        )
    }
}
