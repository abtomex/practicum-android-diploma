package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.domain.models.Vacancies

class VacanciesApiConverter(
    val vacancyCardApiConverter: VacancyCardApiConverter
) : ApiConverter<VacanciesDto?, Vacancies> {
    override fun map(dto: VacanciesDto?): Vacancies {
        return Vacancies(
            found = dto?.found ?: 0,
            pages = dto?.pages ?: 0,
            page = dto?.page ?: 0,
            items = dto?.items?.map { vacancyCardApiConverter.map(it) } ?: emptyList(),
        )
    }
}
