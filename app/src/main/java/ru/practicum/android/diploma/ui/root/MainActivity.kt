package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.presentation.components.BottomNavigationBar
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.favorites.FavoritesScreen
import ru.practicum.android.diploma.presentation.ui.filter.FilterScreen
import ru.practicum.android.diploma.presentation.ui.filter.IndustrySelectionScreen
import ru.practicum.android.diploma.presentation.ui.search.SearchScreen
import ru.practicum.android.diploma.presentation.ui.team.TeamScreen
import ru.practicum.android.diploma.presentation.ui.theme.AppTheme
import ru.practicum.android.diploma.presentation.ui.vacancydetails.VacancyDetailsScreen
import ru.practicum.android.diploma.presentation.viewmodel.FiltersScreenViewModel
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

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
    val shouldShowBottomBar =
        currentRoute != Destination.Filter.route &&
            currentRoute?.startsWith("vacancy_detail") != true &&
            currentRoute != Destination.IndustryFilter.route

    val filtersViewModel: FiltersScreenViewModel = koinViewModel()

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
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Search.route) {
                SearchScreen(
                    navController = navController,
                    filtersViewModel = filtersViewModel
                )
            }
            composable(Destination.Favorites.route) {
                FavoritesScreen(navController = navController)
            }
            composable(Destination.Team.route) {
                TeamScreen()
            }
            composable(Destination.Filter.route) {
                FilterScreen(
                    navController = navController,
                    viewModel = filtersViewModel
                )
            }
            composable(
                route = Destination.VacancyDetails.route,
                arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("vacancyId")?.let {
                    val viewModel: VacancyDetailsViewModel = koinViewModel()
                    VacancyDetailsScreen(
                        vacancyId = it,
                        viewModel = viewModel,
                        onBackPressed = { navController.popBackStack() }
                    )

                }
            }
            composable(route = Destination.IndustryFilter.route) {
                IndustrySelectionScreen(
                    navController = navController,
                    viewModel = koinViewModel()
                )
            }
        }
    }
}
