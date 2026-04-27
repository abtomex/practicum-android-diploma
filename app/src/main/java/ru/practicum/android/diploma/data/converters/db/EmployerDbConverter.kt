package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.domain.models.Employer

class EmployerDbConverter {
    fun employerToEntity(employer: Employer, vacancyId: String = ""): EmployerEntity =
        EmployerEntity(
            id = 0,
            vacancyId = vacancyId,
            name = employer.name,
            logo = employer.logo
        )

    fun entityToEmployer(employerEntity: EmployerEntity): Employer =
        Employer(
            id = employerEntity.id.toString(),
            name = employerEntity.name,
            logo = employerEntity.logo
        )
}
