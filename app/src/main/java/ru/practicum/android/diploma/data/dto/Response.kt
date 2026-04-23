package ru.practicum.android.diploma.data.dto

open class Response<T> (
    val resultCode: Int,
    val body: T?
)
