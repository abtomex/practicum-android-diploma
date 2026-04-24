package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.domain.models.Salary

class SalaryDbConverter {
    fun salaryToEntity(salary: Salary): SalaryEntity =
        SalaryEntity(
            vacancyId = "",
            from = salary.from,
            to = salary.to,
            currency = salary.currency
        )

    fun entityToSalary(salaryEntity: SalaryEntity): Salary =
        Salary(
            from = salaryEntity.from,
            to = salaryEntity.to,
            currency = salaryEntity.currency
        )
}
