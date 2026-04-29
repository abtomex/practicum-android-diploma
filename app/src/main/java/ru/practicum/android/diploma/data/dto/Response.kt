package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.data.dto.industries.IndustryDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsDto

sealed class Response<T> (val resultCode: Int, val body: T?) {

    class AreasResponse(areas: List<AreaDto>) : Response<List<AreaDto>>(SUCCESS_RESPONSE_CODE, areas)
    class IndustriesResponse(industries: List<IndustryDto>) : Response<List<IndustryDto>>(SUCCESS_RESPONSE_CODE, industries)
    class VacanciesResponse(vacancies: VacanciesDto) : Response<VacanciesDto>(SUCCESS_RESPONSE_CODE, vacancies)
    class VacancyDetailsResponse(vacancyDetails: VacancyDetailsDto) : Response<VacancyDetailsDto>(SUCCESS_RESPONSE_CODE, vacancyDetails)
    class ErrorResponse(val errorCode: Int) : Response<Any>(errorCode, null)

    companion object {
        const val SUCCESS_RESPONSE_CODE = 200
        const val STATUS_SERVER_ERROR = 500
        const val STATUS_NETWORK_ERROR = -1
    }

}
