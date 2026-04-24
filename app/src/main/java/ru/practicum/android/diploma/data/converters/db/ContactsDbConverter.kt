package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.relations.ContactsWithPhones
import ru.practicum.android.diploma.domain.models.Contacts

class ContactsDbConverter(
    val phoneDbConverter: PhoneDbConverter
) {
    fun contactsToBareEntity(contacts: Contacts): ContactsEntity =
        ContactsEntity(
            id = contacts.id,
            vacancyId = "",
            name = contacts.name,
            email = contacts.email
        )

    fun bareEntityToContacts(contactsEntity: ContactsEntity): Contacts =
        Contacts(
            id = contactsEntity.id,
            name = contactsEntity.name,
            email = contactsEntity.email,
            phones = emptyList()
        )

    fun fullEntityToContacts(contactsWithPhones: ContactsWithPhones): Contacts =
        Contacts(
            id = contactsWithPhones.contacts.id,
            name = contactsWithPhones.contacts.name,
            email = contactsWithPhones.contacts.email,
            phones = contactsWithPhones.phones.map { phoneDbConverter.entityToPhone(it) }
        )

    fun contactsToFullEntity(contacts: Contacts): ContactsWithPhones {
        val contactsEntity = contactsToBareEntity(contacts)
        val phonesEntities = contacts.phones.map { phoneDbConverter.phoneToEntity(it) }

        return ContactsWithPhones(
            contacts = contactsEntity,
            phones = phonesEntities
        )
    }
}
