package ru.practicum.android.diploma.presentation.navigation

sealed class Destination(val route: String) {
    object Search : Destination("search")
    object Favorites : Destination("favorites")
    object Team : Destination("team")
    object Filter : Destination("filter")
    object IndustryFilter : Destination("industry_filter")
    object VacancyDetails : Destination("vacancy_detail/{vacancyId}") {
        fun createRoute(vacancyId: String): String = "vacancy_detail/$vacancyId"
    }
}
