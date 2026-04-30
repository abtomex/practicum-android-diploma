package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.ScheduleDto
import ru.practicum.android.diploma.domain.models.Schedule

class ScheduleApiConverter : ApiConverter<ScheduleDto?, Schedule?> {
    override fun map(dto: ScheduleDto?): Schedule? {
        if (dto == null) return null
        return Schedule(
            id = dto.id,
            name = dto.name
        )

    }
}
