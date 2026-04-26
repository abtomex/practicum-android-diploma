package ru.practicum.android.diploma.data.dto.areas

class AreaDto(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<AreaDto>
)
