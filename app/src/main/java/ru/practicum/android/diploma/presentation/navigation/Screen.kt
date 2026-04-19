package ru.practicum.android.diploma.presentation.navigation

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Team : Screen("team")
    object Filter : Screen("filter")
}
