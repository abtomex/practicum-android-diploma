package ru.practicum.android.diploma.presentation

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
import ru.practicum.android.diploma.presentation.navigation.Screen
import ru.practicum.android.diploma.presentation.screens.FavoritesScreen
import ru.practicum.android.diploma.presentation.screens.FilterScreen
import ru.practicum.android.diploma.presentation.screens.SearchScreen
import ru.practicum.android.diploma.presentation.screens.TeamScreen
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
    val shouldShowBottomBar = currentRoute != Screen.Filter.route

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Search.route) {
                SearchScreen(navController = navController)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable(Screen.Team.route) {
                TeamScreen()
            }
            composable(Screen.Filter.route) {
                FilterScreen(navController = navController)
            }
        }
    }
}
