package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.domain.models.Area

class AreaApiConverter : ApiConverter<AreaDto, Area> {

    override fun map(dto: AreaDto): Area {
        return Area(
            id = dto.id,
            name = dto.name,
            parentId = dto.parentId,
            areas = dto.areas
        )
    }

}
