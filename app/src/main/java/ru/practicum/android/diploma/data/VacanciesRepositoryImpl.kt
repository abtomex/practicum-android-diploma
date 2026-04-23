package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.converters.VacancyCardApiConverter
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancies.VacancyResponseDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacancyCard

class VacanciesRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: VacancyCardApiConverter
) : VacanciesRepository {

    override suspend fun getAllFromApi(): List<VacancyCard> {
        return (networkClient.doRequest(VacanciesRequestDto()) as VacancyResponseDto)
            .items.map { vacancyCardDto -> apiConverter.map(vacancyCardDto) }
    }

}
