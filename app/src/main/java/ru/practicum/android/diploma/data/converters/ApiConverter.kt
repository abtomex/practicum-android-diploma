package ru.practicum.android.diploma.data.converters

interface ApiConverter<D, I> {
    fun map(dto: D): I

}
