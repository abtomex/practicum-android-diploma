package ru.practicum.android.diploma.data.dto.areas

class AreaDto(
    val id: Int,
    name: String,
    parentId: Int,
    areas: List<AreaDto>
)
