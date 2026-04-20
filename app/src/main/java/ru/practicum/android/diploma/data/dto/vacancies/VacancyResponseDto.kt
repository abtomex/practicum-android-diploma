package ru.practicum.android.diploma.data.dto.vacancies

import ru.practicum.android.diploma.data.dto.Response

class VacancyResponseDto (
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>
) : Response()
