package ru.practicum.android.diploma.presentation.components

import ru.practicum.android.diploma.presentation.navigation.Destination

data class BottomNavItem(
    val title: String,
    val iconResId: Int,
    val destination: Destination
)
