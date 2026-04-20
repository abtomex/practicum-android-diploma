package ru.practicum.android.diploma.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.navigation.Screen
import ru.practicum.android.diploma.presentation.ui.theme.ActiveBlue
import ru.practicum.android.diploma.presentation.ui.theme.IconSizeDefault
import ru.practicum.android.diploma.presentation.ui.theme.InactiveGray

data class BottomNavItem(
    val title: String,
    val iconResId: Int,
    val screen: Screen
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(stringResource(R.string.bottom_nav_home), R.drawable.ic_home, Screen.Search),
        BottomNavItem(stringResource(R.string.bottom_nav_favorites), R.drawable.ic_favorite, Screen.Favorites),
        BottomNavItem(stringResource(R.string.bottom_nav_team), R.drawable.ic_team, Screen.Team)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(IconSizeDefault),
                        tint = if (isSelected) ActiveBlue else InactiveGray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) ActiveBlue else InactiveGray,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
