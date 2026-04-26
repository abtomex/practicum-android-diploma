package ru.practicum.android.diploma.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.data.dto.industries.IndustryDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsDto

interface EndpointsApiService {

    @GET("/areas")
    fun areas(): Call<List<AreaDto>>

    @GET("/industries")
    fun industries(): Call<List<IndustryDto>>

    @GET("/vacancies")
    fun vacancies(@QueryMap params: Map<String, String>): Call<VacanciesDto>

    @GET("/vacancies/{id}")
    fun vacancyDetails(@QueryMap params: Map<String, String>): Call<VacancyDetailsDto>

}
