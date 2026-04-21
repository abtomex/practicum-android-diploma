package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.presentation.components.BottomNavigationBar
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.favorites.FavoritesScreen
import ru.practicum.android.diploma.presentation.ui.filter.FilterScreen
import ru.practicum.android.diploma.presentation.ui.search.SearchScreen
import ru.practicum.android.diploma.presentation.ui.team.TeamScreen
import ru.practicum.android.diploma.presentation.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = currentRoute != Destination.Filter.route

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Search.route,
            modifier = Modifier.Companion.padding(innerPadding)
        ) {
            composable(Destination.Search.route) {
                SearchScreen(navController = navController)
            }
            composable(Destination.Favorites.route) {
                FavoritesScreen()
            }
            composable(Destination.Team.route) {
                TeamScreen()
            }
            composable(Destination.Filter.route) {
                FilterScreen(navController = navController)
            }
        }
    }
}
