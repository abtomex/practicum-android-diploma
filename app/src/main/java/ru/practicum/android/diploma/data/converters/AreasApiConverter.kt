package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.domain.models.Area

class AreasApiConverter: ApiConverter<AreaDto, Area> {

    override fun map(dto: AreaDto): Area {
        return Area(
            id = dto.id,
            name = dto.name,
            parentId = dto.parentId,
            areas = dto.areas
        )
    }

}
