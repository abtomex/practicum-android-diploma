package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.SkillEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.db.relations.VacancyCardWithSalary
import ru.practicum.android.diploma.data.db.relations.VacancyWithDetails

@Dao
interface VacancyDao {
    @Transaction
    @Query("SELECT * FROM vacancy_detail WHERE id = :id")
    suspend fun getVacancyWithDetails(id: String): VacancyWithDetails?

    @Transaction
    @Query("SELECT * FROM vacancy_card")
    suspend fun getVacancyCards(): List<VacancyCardWithSalary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalary(salary: SalaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<SkillEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhones(phones: List<PhoneEntity>)

    @Transaction
    suspend fun insertFullVacancy(vacancyWithDetails: VacancyWithDetails) {
        vacancyWithDetails.salary?.let { insertSalary(it) }
        vacancyWithDetails.address?.let { insertAddress(it) }
        vacancyWithDetails.experience?.let { insertExperience(it) }
        vacancyWithDetails.schedule?.let { insertSchedule(it) }
        vacancyWithDetails.employment?.let { insertEmployment(it) }
        vacancyWithDetails.employer?.let { insertEmployer(it) }
        vacancyWithDetails.area?.let { insertArea(it) }
        vacancyWithDetails.industry?.let { insertIndustry(it) }

        vacancyWithDetails.contacts?.let { contacts ->
            insertContacts(contacts)
            insertPhones(vacancyWithDetails.contacts?.phones ?: emptyList())
        }

        insertVacancy(vacancyWithDetails.vacancy)
        insertSkills(vacancyWithDetails.skills)
    }
}
