package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.vacancies.VacancyCardSalaryDto
import ru.practicum.android.diploma.domain.models.Salary

class SalaryApiConverter: ApiConverter<VacancyCardSalaryDto, Salary> {
    override fun map(dto: VacancyCardSalaryDto?): Salary? {
        if (dto == null) return null
        return Salary (
            from = dto.from,
            to = dto.to,
            currency = dto.currency,
        )
    }
}
