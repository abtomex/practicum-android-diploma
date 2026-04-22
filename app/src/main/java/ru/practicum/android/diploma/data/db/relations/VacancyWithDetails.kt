package ru.practicum.android.diploma.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

data class VacancyWithDetails(
    @Embedded
    val vacancy: VacancyDetailEntity,

    @Relation(parentColumn = "id", entityColumn = "vacancyId")
    val salary: SalaryEntity?,

    @Relation(parentColumn = "id", entityColumn = "vacancyId")
    val address: AddressEntity?,

    @Relation(entity = EmployerEntity::class, parentColumn = "employerId", entityColumn = "id")
    val employerWithPhones: EmployerWithPhones
)
