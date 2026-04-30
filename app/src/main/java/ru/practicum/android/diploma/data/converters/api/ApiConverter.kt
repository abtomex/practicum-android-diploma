package ru.practicum.android.diploma.data.converters.api

interface ApiConverter<D, I> {
    fun map(dto: D): I

}
