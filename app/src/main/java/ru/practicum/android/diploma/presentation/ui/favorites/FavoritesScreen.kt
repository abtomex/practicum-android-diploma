package ru.practicum.android.diploma.presentation.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.components.VacancyCardItem
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.viewmodel.FavoritesState
import ru.practicum.android.diploma.presentation.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorites_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 26.sp,
                        color = BlackPrimary
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is FavoritesState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is FavoritesState.Empty -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(PaddingMedium)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_empty_favorites),
                            contentDescription = null,
                            modifier = Modifier.size(328.dp, 223.dp)
                        )
                        Text(
                            text = stringResource(R.string.favorites_empty),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 26.sp,
                            modifier = Modifier.padding(top = PaddingMedium)
                        )
                    }
                }
                is FavoritesState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(PaddingMedium)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_error_favorite),
                            contentDescription = null,
                            modifier = Modifier.size(328.dp, 223.dp)
                        )
                        Text(
                            text = stringResource(R.string.favorites_error),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 26.sp,
                            modifier = Modifier.padding(top = PaddingMedium)
                        )
                    }
                }
                is FavoritesState.Success -> {
                    val vacancies = (state as FavoritesState.Success).vacancies
                    if (vacancies.isEmpty()) {
                        Text(
                            text = stringResource(R.string.favorites_empty),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 26.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(PaddingMedium)
                        )
                    } else {
                        LazyColumn {
                            items(vacancies) { vacancy ->
                                VacancyCardItem(
                                    vacancy = vacancy,
                                    onItemClick = { vacancyId ->
                                        navController.navigate(Destination.VacancyDetails.createRoute(vacancyId))
                                    },
                                    showFavoriteButton = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
