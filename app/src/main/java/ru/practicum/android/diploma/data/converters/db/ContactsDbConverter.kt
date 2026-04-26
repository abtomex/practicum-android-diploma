package ru.practicum.android.diploma.data.converters.db

import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.relations.ContactsWithPhones
import ru.practicum.android.diploma.domain.models.Contacts

class ContactsDbConverter(
    private val phoneDbConverter: PhoneDbConverter
) {
    fun contactsToBareEntity(contacts: Contacts, vacancyId: String = ""): ContactsEntity =
        ContactsEntity(
            id = 0,
            vacancyId = vacancyId,
            name = contacts.name,
            email = contacts.email
        )

    fun fullEntityToContacts(contactsWithPhones: ContactsWithPhones): Contacts =
        Contacts(
            id = contactsWithPhones.contacts.id.toString(),
            name = contactsWithPhones.contacts.name,
            email = contactsWithPhones.contacts.email,
            phones = contactsWithPhones.phones.map { phoneDbConverter.entityToPhone(it) }
        )

    fun contactsToFullEntity(contacts: Contacts, vacancyId: String = ""): ContactsWithPhones {
        val contactsId = 0L

        val contactsEntity = contactsToBareEntity(contacts, vacancyId)
        val phonesEntities = contacts.phones.map { phoneDbConverter.phoneToEntity(it, contactsId) }

        return ContactsWithPhones(
            contacts = contactsEntity,
            phones = phonesEntities
        )
    }
}
