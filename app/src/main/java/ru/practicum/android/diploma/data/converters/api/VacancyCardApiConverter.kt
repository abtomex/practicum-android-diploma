package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancies.VacancyCardDto
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyCard

class VacancyCardApiConverter : ApiConverter<VacancyCardDto, VacancyCard> {
    override fun map(dto: VacancyCardDto): VacancyCard {
        return VacancyCard(
            id = dto.id,
            name = dto.name,
            company = dto.company,
            city = dto.city,
            salary = Salary(
                from = dto.salary?.from,
                to = dto.salary?.to,
                currency = dto.salary?.currency
            ),
            logo = dto.logo
        )
    }
}
