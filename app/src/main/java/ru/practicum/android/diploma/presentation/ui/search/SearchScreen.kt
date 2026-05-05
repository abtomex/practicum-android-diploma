package ru.practicum.android.diploma.presentation.ui.search

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.theme.ActiveBlue
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.WhiteUniversal
import ru.practicum.android.diploma.presentation.viewmodel.FiltersScreenViewModel
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchFailuresEnum

val YsDisplayMedium = FontFamily(
    Font(R.font.ys_display_medium)
)

private class MockSearchInteractor : SearchInteractor {
    override fun searchByPages(vacancyRequestByPages: VacancyRequestByPages): Flow<ApiResponse<Vacancies>> {
        return flowOf(ApiResponse.Success(Vacancies(items = emptyList(), found = 0, pages = 1, page = 1)))
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun SearchScreenPrev() {
    val mockInteractor = MockSearchInteractor()
    val mockFiltersViewModel = FiltersScreenViewModel()
    val testViewModel = SearchViewModel(
        searchInteractor = mockInteractor
    )

    SearchScreen(
        navController = rememberNavController(),
        viewModel = testViewModel,
        filtersViewModel = mockFiltersViewModel
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = koinViewModel(),
    filtersViewModel: FiltersScreenViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val requestStr by viewModel.searchRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val noInternetMessage = stringResource(R.string.error_no_internet)
    val errorMessage = stringResource(R.string.error_error)
    val filterData by filtersViewModel.filtersApplied.collectAsStateWithLifecycle()
    val hasActiveFilters by filtersViewModel.hasActiveFilters.collectAsStateWithLifecycle()

    LaunchedEffect(filterData) {
        Log.d("SearchScreen", "LaunchedEffect triggered with: $filterData")
        filterData?.let {
            viewModel.updateFilters(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { event ->
            val message = when (event) {
                SearchFailuresEnum.NO_INTERNET -> noInternetMessage
                SearchFailuresEnum.SERVER_ERROR -> errorMessage
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(end = 0.dp)
                    .height(64.dp)
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.search_title),
                        fontSize = 22.sp,
                        color = BlackPrimary,
                        fontFamily = YsDisplayMedium
                    )
                },
                actions = {
                    FilterButton(
                        onClick = { navController.navigate(Destination.Filter.route) },
                        contentColor = if (hasActiveFilters) WhiteUniversal else BlackPrimary,
                        containerColor = if (hasActiveFilters) ActiveBlue else WhiteUniversal
                    )
                },

            )
        }
    ) { innerPadding ->
        SearchScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state,
            searchQuery = requestStr,
            onQueryChanged = viewModel::onSearchQueryChanged,
            onClearSearch = viewModel::onClearSearch,
            onLoadNextPage = viewModel::onLoadNextPage,
            onVacancyClick = { navController.navigate(Destination.VacancyDetails.createRoute(it)) },
        )
    }
}

@Composable
fun FilterButton(
    onClick: () -> Unit,
    contentColor: Color,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .padding(end = 18.dp)
            .size(24.dp)
            .background(
                color = containerColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_1),
            contentDescription = "Фильтр",
            modifier = Modifier.size(height = 12.dp, width = 18.dp), // Размер иконки
            tint = contentColor
        )
    }
}
