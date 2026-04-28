package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

class VacancyRequestApiConverter : ApiConverter<VacancyRequestByPages, VacanciesRequestDto> {
    override fun map(dto: VacancyRequestByPages): VacanciesRequestDto {
        return VacanciesRequestDto (
            area = dto.area,
            industry = dto.industry,
            text = dto.text,
            salary = dto.salary,
            page = dto.page,
            onlyWithSalary = dto.onlyWithSalary
        )
    }
}
