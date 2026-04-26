package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.domain.models.Address

class AddressDbConverter {
    fun addressToEntity(address: Address, vacancyId: String = ""): AddressEntity =
        AddressEntity(
            id = 0,
            vacancyId = vacancyId,
            city = address.city,
            street = address.street,
            building = address.building,
            raw = address.raw
        )

    fun entityToAddress(addressEntity: AddressEntity): Address =
        Address(
            id = addressEntity.id.toString(),
            city = addressEntity.city,
            street = addressEntity.street,
            building = addressEntity.building,
            raw = addressEntity.raw
        )
}
