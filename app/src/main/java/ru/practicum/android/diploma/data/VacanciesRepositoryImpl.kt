package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.VacancyCardApiConverter
import ru.practicum.android.diploma.data.db.dao.VacancyCardDao
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.db.relations.ContactsWithPhones
import ru.practicum.android.diploma.data.db.relations.VacancyWithDetails
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.*

class VacanciesRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: VacancyCardApiConverter,
    private val vacancyCardDao: VacancyCardDao,
    private val vacancyDetailDao: VacancyDetailDao
) : VacanciesRepository {

    override suspend fun getAllFromApi(): List<VacancyCard>? {
        return (networkClient.doRequest(VacanciesRequestDto()) as Response.VacanciesResponse)
            .body?.items?.map { vacancyCardDto -> apiConverter.map(vacancyCardDto) }
    }

    override fun getAllVacancyCards(): Flow<List<VacancyCard>> {
        return vacancyCardDao.getVacancyCards().map { entities ->
            entities.map { entity ->
                VacancyCard(
                    id = entity.id,
                    name = entity.name,
                    company = entity.company,
                    city = entity.city,
                    salary = Salary(
                        from = entity.salaryFrom,
                        to = entity.salaryTo,
                        currency = entity.salaryCurrency
                    ),
                    logo = entity.logo
                )
            }
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetails> {
        return vacancyDetailDao.getVacancyWithDetails(vacancyId).map { vacancyWithDetails ->
            vacancyWithDetails?.toDomain()
                ?: throw Exception("Vacancy not found in database")
        }
    }

    override suspend fun addVacancyToFavorites(vacancy: VacancyDetails) {
        vacancyCardDao.insertVacancyCard(vacancy.toCardEntity())
        vacancyDetailDao.insertFullVacancy(vacancy.toDetailsEntity())
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: String) {
        vacancyCardDao.deleteVacancyCard(vacancyId)
        vacancyDetailDao.deleteVacancy(vacancyId)
    }

    //сохранение

    private fun VacancyDetails.toCardEntity(): VacancyCardEntity {
        return VacancyCardEntity(
            id = id,
            name = name,
            company = employer.name,
            logo = employer.logo,
            city = area.name,
            salaryFrom = salary?.from,
            salaryTo = salary?.to,
            salaryCurrency = salary?.currency
        )
    }

    private fun VacancyDetails.toDetailsEntity(): VacancyWithDetails {
        return VacancyWithDetails(
            vacancy = VacancyDetailEntity(
                id = id,
                name = name,
                description = description,
                experience = experience?.name,
                schedule = schedule?.name,
                employment = employment?.name,
                skillsList = skills,
                area = area.name,
                url = url,
                industry = industry.name
            ),
            salary = salary?.let {
                SalaryEntity(
                    id = "${id}_salary",
                    vacancyId = id,
                    from = it.from,
                    to = it.to,
                    currency = it.currency
                )
            },
            address = address?.let {
                AddressEntity(
                    id = it.id,
                    vacancyId = id,
                    city = it.city,
                    street = it.street,
                    building = it.building,
                    raw = it.raw
                )
            },
            employer = EmployerEntity(
                id = employer.id,
                vacancyId = id,
                name = employer.name,
                logo = employer.logo
            ),
            contactsWithPhones = contacts?.let { contact ->
                ContactsWithPhones(
                    contacts = ContactsEntity(
                        id = contact.id,
                        vacancyId = id,
                        name = contact.name,
                        email = contact.email
                    ),
                    phones = contact.phones.map { phone ->
                        PhoneEntity(
                            id = System.currentTimeMillis(),
                            contactsId = contact.id,
                            comment = phone.comment,
                            formatted = phone.formatted
                        )
                    }
                )
            } ?: ContactsWithPhones(
                contacts = ContactsEntity("empty", id, "", ""),
                phones = emptyList()
            )
        )
    }

    private fun VacancyWithDetails.toDomain(): VacancyDetails {
        return VacancyDetails(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            salary = salary?.let { Salary(it.from, it.to, it.currency) },
            address = address?.let { Address(it.id, it.city, it.street, it.building, it.raw) },
            experience = vacancy.experience?.let { Experience("", it) },
            schedule = vacancy.schedule?.let { Schedule("", it) },
            employment = vacancy.employment?.let { Employment("", it) },
            contacts = contactsWithPhones.contacts.id.takeIf { it != "empty" }?.let {
                Contacts(
                    id = it,
                    name = contactsWithPhones.contacts.name,
                    email = contactsWithPhones.contacts.email,
                    phones = contactsWithPhones.phones.map { phone ->
                        Phone(phone.comment, phone.formatted)
                    }
                )
            },
            employer = Employer(employer.id, employer.name, employer.logo),
            area = Area(0, vacancy.area, 0, emptyList()),
            skills = vacancy.skillsList,
            url = vacancy.url,
            industry = Industry(0, vacancy.industry)
        )
    }
}
