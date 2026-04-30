package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.SalaryDetailDto
import ru.practicum.android.diploma.domain.models.Salary

class SalaryApiConverter : ApiConverter<SalaryDetailDto?, Salary?> {
    override fun map(dto: SalaryDetailDto?): Salary? {
        if (dto == null) return null
        return Salary(
            from = dto.from,
            to = dto.to,
            currency = dto.currency,
        )
    }
}
