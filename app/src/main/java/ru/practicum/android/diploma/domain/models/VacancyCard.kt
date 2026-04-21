package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.vacancies.VacancyCardSalaryDto

data class VacancyCard (
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: VacancyCardSalaryDto?,
    val logo: String?
)
