package ru.practicum.android.diploma.data.dto.vacancydetails

import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.data.dto.industries.IndustryDto

data class VacancyDetailsDto(

    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDetailDto?,
    val address: AddressDto?,
    val experience: ExperienceDto?,
    val schedule: ScheduleDto?,
    val employment: EmploymentDto?,
    val contacts: ContactsDto?,
    val employer: EmployerDto,
    val area: AreaDto,
    val skills: List<String>,
    val url: String,
    val industry: IndustryDto,
)
