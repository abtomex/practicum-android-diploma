package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyCardEntity

@Dao
interface VacancyCardDao {
    @Query("SELECT * FROM vacancy_card")
    fun getVacancyCards(): Flow<List<VacancyCardEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVacancyCard(vacancyCard: VacancyCardEntity)

    @Query("DELETE FROM vacancy_card WHERE id = :vacancyCardId")
    suspend fun deleteVacancyCard(vacancyCardId: String)
}
