package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.areas.AreaDto

data class Area(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<AreaDto>
)
