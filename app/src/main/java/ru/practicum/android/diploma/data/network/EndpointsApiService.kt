package ru.practicum.android.diploma.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.areas.AreasResponseDto
import ru.practicum.android.diploma.data.dto.industries.IndustriesResponseDto
import ru.practicum.android.diploma.data.dto.vacancies.VacancyResponseDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsResponseDto

interface EndpointsApiService {

    @GET("/areas")
    fun areas(): Call<AreasResponseDto>

    @GET("/industries")
    fun industries(): Call<IndustriesResponseDto>

    @GET("/vacancies")
    fun vacancies(@QueryMap params: Map<String, String>): Call<VacancyResponseDto>

    @GET("/vacancies/{id}")
    fun vacancyDetails(@QueryMap params: Map<String, String>): Call<VacancyDetailsResponseDto>

}
