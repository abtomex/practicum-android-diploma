package ru.practicum.android.diploma.data.converters.api

import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsDto
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyDetailsApiConverter(
    val salaryApiConverter: SalaryApiConverter,
    val addressApiConverter: AddressApiConverter,
    val experienceApiConverter: ExperienceApiConverter,
    val scheduleApiConverter: ScheduleApiConverter,
    val employmentApiConverter: EmploymentApiConverter,
    val contactsApiConverter: ContactsApiConverter,
    val employerApiConverter: EmployerApiConverter,
    val areaApiConverter: AreaApiConverter,
    val industryApiConverter: IndustryApiConverter,
) : ApiConverter<VacancyDetailsDto, VacancyDetails> {
    override fun map(dto: VacancyDetailsDto): VacancyDetails {
        return VacancyDetails(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            salary = salaryApiConverter.map(dto.salary),
            address = addressApiConverter.map(dto.address),
            experience = experienceApiConverter.map(dto.experience),
            schedule = scheduleApiConverter.map(dto.schedule),
            employment = employmentApiConverter.map(dto.employment),
            contacts = contactsApiConverter.map(dto.contacts),
            employer = employerApiConverter.map(dto.employer),
            area = areaApiConverter.map(dto.area),
            skills = dto.skills,
            url = dto.url,
            industry = industryApiConverter.map(dto.industry),
        )
    }
}
