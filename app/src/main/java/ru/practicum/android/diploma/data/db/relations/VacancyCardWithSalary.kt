package ru.practicum.android.diploma.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity

data class VacancyCardWithSalary(
    @Embedded
    val vacancyCard: VacancyCardEntity,
    @Relation(parentColumn = "salaryId", entityColumn = "id")
    val salary: SalaryEntity
)
