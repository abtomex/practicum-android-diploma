package ru.practicum.android.diploma.data.dto.areas

import ru.practicum.android.diploma.data.dto.Response

class AreasResponseDto(
    resultCode: Int,
    val areas: List<AreaDto>
) : Response<List<AreaDto>>(resultCode, areas)
