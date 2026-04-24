package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.SalaryEntity
import ru.practicum.android.diploma.data.db.entity.VacancyDetailsEntity
import ru.practicum.android.diploma.data.db.relations.VacancyWithDetails

@Dao
interface VacancyDetailDao {
    @Transaction
    @Query("SELECT * FROM vacancy_detail WHERE id = :id")
    fun getVacancyWithDetails(id: String): Flow<VacancyWithDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailsEntity)

    @Transaction
    @Query("DELETE FROM vacancy_detail WHERE id = :vacancyId")
    suspend fun deleteVacancy(vacancyId: String)

    // region Insert nested objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalary(salary: SalaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployer(employer: EmployerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: ContactsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhones(phones: List<PhoneEntity>)
    // endregion

    @Transaction
    suspend fun insertFullVacancy(vacancyWithDetails: VacancyWithDetails) {
        insertVacancy(vacancyWithDetails.vacancy)
        insertEmployer(vacancyWithDetails.employer)

        vacancyWithDetails.salary?.let { insertSalary(it) }
        vacancyWithDetails.address?.let { insertAddress(it) }

        vacancyWithDetails.contactsWithPhones?.let { contacts ->
            insertContacts(contacts.contacts)
            insertPhones(contacts.phones)
        }
    }
}
