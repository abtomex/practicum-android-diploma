package ru.practicum.android.diploma.data.dto.vacancies

import ru.practicum.android.diploma.data.dto.Response

class VacancyResponseDto(
    resultCode: Int,
    vacancies: VacanciesDto
) : Response<VacanciesDto>(resultCode, vacancies)
