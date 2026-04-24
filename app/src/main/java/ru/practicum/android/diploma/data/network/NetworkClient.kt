package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto

interface NetworkClient {
    suspend fun doRequest(dto: Request): Response
    suspend fun doRequestList(dto: AreasRequestDto): Response
}
