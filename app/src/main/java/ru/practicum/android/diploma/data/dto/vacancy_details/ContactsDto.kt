package ru.practicum.android.diploma.data.dto.vacancy_details

data class ContactsDto (
    val id: String,
    val name: String,
    val email: String,
    val phones: List<PhoneDto>
)
