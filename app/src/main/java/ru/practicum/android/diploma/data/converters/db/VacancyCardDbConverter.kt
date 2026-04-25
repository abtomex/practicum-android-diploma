package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyCardDbConverter {
    fun vacancyCardToEntity(vacancyCard: VacancyCard): VacancyCardEntity =
        VacancyCardEntity(
            id = vacancyCard.id,
            name = vacancyCard.name,
            company = vacancyCard.company,
            logo = vacancyCard.logo,
            city = vacancyCard.city,
            salaryFrom = vacancyCard.salary?.from,
            salaryTo = vacancyCard.salary?.to,
            salaryCurrency = vacancyCard.salary?.currency
        )

    fun entityToVacancyCard(vacancyCardEntity: VacancyCardEntity): VacancyCard {
        val salary = if (vacancyCardEntity.salaryFrom != null || vacancyCardEntity.salaryTo != null)
            Salary(
                from = vacancyCardEntity.salaryFrom,
                to = vacancyCardEntity.salaryTo,
                currency = vacancyCardEntity.salaryCurrency
            ) else null
        return VacancyCard(
            id = vacancyCardEntity.id,
            name = vacancyCardEntity.name,
            company = vacancyCardEntity.company,
            logo = vacancyCardEntity.logo,
            city = vacancyCardEntity.city,
            salary = salary
        )
    }

    fun vacancyDetailsToVacancyCard(vacancyDetails: VacancyDetails): VacancyCard =
        VacancyCard(
            id = vacancyDetails.id,
            name = vacancyDetails.name,
            company = vacancyDetails.employer.name,
            logo = vacancyDetails.employer.logo,
            city = vacancyDetails.address?.city,
            salary = vacancyDetails.salary
        )
}
