package ru.practicum.android.diploma.data.converters.api

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsDto
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyDetailsApiConverter : ApiConverter<VacancyDetailsDto, VacancyDetails>, KoinComponent {

    private val salaryApiConverter: SalaryApiConverter by inject()
    private val addressApiConverter: AddressApiConverter by inject()
    private val experienceApiConverter: ExperienceApiConverter by inject()
    private val scheduleApiConverter: ScheduleApiConverter by inject()
    private val employmentApiConverter: EmploymentApiConverter by inject()
    private val contactsApiConverter: ContactsApiConverter by inject()
    private val employerApiConverter: EmployerApiConverter by inject()
    private val areaApiConverter: AreaApiConverter by inject()
    private val industryApiConverter: IndustryApiConverter by inject()

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
