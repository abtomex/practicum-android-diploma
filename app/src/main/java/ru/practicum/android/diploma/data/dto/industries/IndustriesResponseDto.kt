package ru.practicum.android.diploma.data.dto.industries

import ru.practicum.android.diploma.data.dto.Response

class IndustriesResponseDto (
    resultCode: Int,
    val industries: List<IndustryDto>
) : Response<List<IndustryDto>>(resultCode, industries)
