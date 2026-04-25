package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.VacancyDetailsEntity
import ru.practicum.android.diploma.data.db.relations.VacancyWithDetails
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyDetailsDbConverter(
    private val salaryDbConverter: SalaryDbConverter,
    private val addressDbConverter: AddressDbConverter,
    private val employerDbConverter: EmployerDbConverter,
    private val contactsDbConverter: ContactsDbConverter
) {
    fun vacancyDetailsToBareEntity(vacancyDetails: VacancyDetails): VacancyDetailsEntity =
        VacancyDetailsEntity(
            id = vacancyDetails.id,
            name = vacancyDetails.name,
            description = vacancyDetails.description,
            experience = vacancyDetails.experience?.name,
            schedule = vacancyDetails.schedule?.name,
            employment = vacancyDetails.employment?.name,
            skillsList = vacancyDetails.skills,
            area = vacancyDetails.area.name,
            industry = vacancyDetails.industry.name,
            url = vacancyDetails.url
        )

    fun vacancyDetailsToFullEntity(vacancyDetails: VacancyDetails): VacancyWithDetails {
        val vacancyId = vacancyDetails.id

        val vacancyEntity = vacancyDetailsToBareEntity(vacancyDetails)
        val employerEntity = employerDbConverter.employerToEntity(vacancyDetails.employer, vacancyId)

        val salaryEntity = vacancyDetails.salary?.let { salaryDbConverter.salaryToEntity(it, vacancyId) }
        val addressEntity = vacancyDetails.address?.let { addressDbConverter.addressToEntity(it, vacancyId) }
        val contactsFullEntity = vacancyDetails.contacts?.let { contactsDbConverter.contactsToFullEntity(it, vacancyId) }

        return VacancyWithDetails(
            vacancy = vacancyEntity,
            employer = employerEntity,
            salary = salaryEntity,
            address = addressEntity,
            contactsWithPhones = contactsFullEntity
        )
    }

    fun fullEntityToVacancyDetails(vacancyWithDetails: VacancyWithDetails): VacancyDetails {
        val employer = employerDbConverter.entityToEmployer(vacancyWithDetails.employer)

        val salary = vacancyWithDetails.salary?.let { salaryDbConverter.entityToSalary(it) }
        val address = vacancyWithDetails.address?.let { addressDbConverter.entityToAddress(it) }
        val contacts = vacancyWithDetails.contactsWithPhones?.let { contactsDbConverter.fullEntityToContacts(it) }

        val experience = vacancyWithDetails.vacancy.experience?.let { Experience("", it) }
        val schedule = vacancyWithDetails.vacancy.schedule?.let { Schedule("", it) }
        val employment = vacancyWithDetails.vacancy.employment?.let { Employment("", it) }

        val area = Area(
            id = 0,
            name = vacancyWithDetails.vacancy.area,
            parentId = 0,
            areas = listOf()
        )

        val industry = Industry(
            id = 0,
            name = vacancyWithDetails.vacancy.industry
        )


        return VacancyDetails(
            id = vacancyWithDetails.vacancy.id,
            name = vacancyWithDetails.vacancy.name,
            description = vacancyWithDetails.vacancy.description,
            skills = vacancyWithDetails.vacancy.skillsList,
            url = vacancyWithDetails.vacancy.url,

            experience = experience,
            schedule = schedule,
            employment = employment,

            area = area,
            industry = industry,

            employer = employer,
            salary = salary,
            address = address,
            contacts = contacts
        )
    }
}
