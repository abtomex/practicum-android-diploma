package ru.practicum.android.diploma.data.dto.vacancydetails

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.data.dto.industries.IndustryDto

class VacancyDetailsResponseDto(
    resultCode: Int,
    val vacancyDetails: VacancyDetailsDto
): Response (resultCode)
