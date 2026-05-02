package ru.practicum.android.diploma.presentation.ui.search

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.IconSizeDefault
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchFailuresEnum
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState

val YsDisplayMedium = FontFamily(
    Font(R.font.ys_display_medium)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val requestStr by viewModel.searchRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val noInternetMessage = stringResource(R.string.error_no_internet)
    val errorMessage = stringResource(R.string.error_error)

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
                title = {
                    Text(
                        text = stringResource(R.string.search_title),
                        fontSize = 22.sp,
                        color = BlackPrimary,
                        fontFamily = YsDisplayMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Destination.Filter.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.size(IconSizeDefault),
                            tint = BlackPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchField(
                searchStr = requestStr,
                onValueChange = viewModel::onSearchQueryChanged,
                onClear = viewModel::onClearSearch
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is SearchState.Default -> DefaultContent()
                is SearchState.Loading -> LoadingContent()
                is SearchState.Content -> {
                    val contentState = state as SearchState.Content
                    SearchResultsContent(
                        vacancyCards = contentState.data,
                        found = contentState.found,
                        isNextPageLoading = contentState.isNextPageLoading,
                        onVacancyClick = { vacancyId: String ->
                            navController.navigate(Destination.VacancyDetails.createRoute(vacancyId))
                        },
                        onLoadNextPage = viewModel::onLoadNextPage
                    )
                }
                is SearchState.Empty -> EmptyContent()
                is SearchState.NoInternet -> NoInternetContent()
                is SearchState.Error -> ErrorContent()
            }
        }
    }
}
