package ru.practicum.android.diploma.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.EmploymentEntity
import ru.practicum.android.diploma.data.db.entity.ExperienceEntity
import ru.practicum.android.diploma.data.db.entity.FilterAreaEntity
import ru.practicum.android.diploma.data.db.entity.FilterIndustryEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.ScheduleEntity
import ru.practicum.android.diploma.data.db.entity.SkillEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

data class VacancyWithDetails(
    @Embedded
    val vacancy: VacancyDetailEntity,

    @Relation(parentColumn = "salaryId", entityColumn = "id")
    val salary: SalaryEntity?,

    @Relation(parentColumn = "addressId", entityColumn = "id")
    val address: AddressEntity?,

    @Relation(parentColumn = "experienceId", entityColumn = "id")
    val experience: ExperienceEntity?,

    @Relation(parentColumn = "scheduleId", entityColumn = "id")
    val schedule: ScheduleEntity?,

    @Relation(parentColumn = "employmentId", entityColumn = "id")
    val employment: EmploymentEntity?,

    @Relation(parentColumn = "contactsId", entityColumn = "id")
    val contacts: ContactsEntity?,

    @Relation(parentColumn = "employerId", entityColumn = "id")
    val employer: EmployerEntity,

    @Relation(parentColumn = "areaId", entityColumn = "id")
    val area: FilterAreaEntity,

    @Relation(parentColumn = "industryId", entityColumn = "id")
    val industry: FilterIndustryEntity,

    @Relation(parentColumn = "id", entityColumn = "vacancyId")
    val skills: List<SkillEntity>
)
