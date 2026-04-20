package ru.practicum.android.diploma.data.db.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import ru.practicum.android.diploma.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity

data class EmployerWithPhones(
    @Embedded
    val employer: EmployerEntity,

    @Relation(parentColumn = "id", entityColumn = "employerId")
    val phones: List<PhoneEntity>
)
