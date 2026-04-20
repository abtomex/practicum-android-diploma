package ru.practicum.android.diploma.data.dto.vacancy_details

import ru.practicum.android.diploma.data.dto.Request

data class VacancyDetailsRequestDto (
    var id: String
) : Request() {
    fun toQueryMap(): Map<String, String> {
        return buildMap {
            put("id", id)
        }
    }

}
