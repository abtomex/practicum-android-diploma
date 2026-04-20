package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.db.relations.VacancyCardWithSalary
import ru.practicum.android.diploma.data.db.relations.VacancyWithDetails

@Dao
interface VacancyDao {
    @Transaction
    @Query("SELECT * FROM vacancy_detail WHERE id = :id")
    fun getVacancyWithDetails(id: String): Flow<VacancyWithDetails?>

    @Transaction
    @Query("SELECT * FROM vacancy_card")
    fun getVacancyCards(): Flow<List<VacancyCardWithSalary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancyCard(vacancyCard: VacancyCardEntity)

    @Query("DELETE FROM vacancy_detail WHERE id = :vacancyId")
    suspend fun deleteVacancy(vacancyId: String)

    @Query("DELETE FROM vacancy_card WHERE id = :vacancyCardId")
    suspend fun deleteVacancyCard(vacancyCardId: String)

    // region Insert nested objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalary(salary: SalaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployer(employer: EmployerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhones(phones: List<PhoneEntity>)
    // endregion

    // region Delete nested objects
    @Query("DELETE FROM salary WHERE id = :salaryId")
    suspend fun deleteSalary(salaryId: String)

    @Query("DELETE FROM address WHERE id = :addressId")
    suspend fun deleteAddress(addressId: String)

    @Query("DELETE FROM employer WHERE id = :employerId")
    suspend fun deleteEmployer(employerId: String)

    @Delete
    suspend fun deletePhones(phones: List<PhoneEntity>)
    // endregion

    @Transaction
    suspend fun insertFullVacancy(vacancyWithDetails: VacancyWithDetails) {
        vacancyWithDetails.salary?.let { insertSalary(it) }
        vacancyWithDetails.address?.let { insertAddress(it) }

        vacancyWithDetails.employerWithPhones.let { employerWithPhones ->
            insertEmployer(employerWithPhones.employer)
            insertPhones(employerWithPhones.phones)
        }

        insertVacancy(vacancyWithDetails.vacancy)
    }

    @Transaction
    suspend fun insertVacancyCardWithSalary(vacancyCardWithSalary: VacancyCardWithSalary) {
        vacancyCardWithSalary.salary?.let { insertSalary(it) }

        insertVacancyCard(vacancyCardWithSalary.vacancyCard)
    }

    @Transaction
    suspend fun deleteFullVacancy(vacancyWithDetails: VacancyWithDetails) {
        vacancyWithDetails.salary?.let { deleteSalary(it.id) }
        vacancyWithDetails.address?.let { deleteAddress(it.id) }

        vacancyWithDetails.employerWithPhones.let { employerWithPhones ->
            deleteEmployer(employerWithPhones.employer.id)
            deletePhones(employerWithPhones.phones)
        }

        deleteVacancyCard(vacancyWithDetails.vacancy.id)
        deleteVacancy(vacancyWithDetails.vacancy.id)
    }
}
