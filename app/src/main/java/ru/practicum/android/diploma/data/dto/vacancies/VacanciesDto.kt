package ru.practicum.android.diploma.data.dto.vacancies

class VacanciesDto(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>
)
