package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.relations.ContactsWithPhones
import ru.practicum.android.diploma.domain.models.Contacts

class ContactsDbConverter(
    private val phoneDbConverter: PhoneDbConverter
) {
    fun contactsToBareEntity(contacts: Contacts, vacancyId: String = ""): ContactsEntity =
        ContactsEntity(
            id = contacts.id,
            vacancyId = vacancyId,
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

    fun contactsToFullEntity(contacts: Contacts, vacancyId: String = ""): ContactsWithPhones {
        val contactsId = contacts.id

        val contactsEntity = contactsToBareEntity(contacts, vacancyId)
        val phonesEntities = contacts.phones.map { phoneDbConverter.phoneToEntity(it, contactsId) }

        return ContactsWithPhones(
            contacts = contactsEntity,
            phones = phonesEntities
        )
    }
}
