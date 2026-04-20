package ru.practicum.android.diploma.data.dto.vacancydetails

data class ContactsDto(
    val id: String,
    val name: String,
    val email: String,
    val phones: List<PhoneDto>
)
