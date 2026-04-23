package ru.practicum.android.diploma.presentation.navigation

sealed class Destination(val route: String) {
    object Search : Destination("search")
    object Favorites : Destination("favorites")
    object Team : Destination("team")
    object Filter : Destination("filter")
}
