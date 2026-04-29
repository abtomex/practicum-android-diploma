package ru.practicum.android.diploma.presentation.ui.search

import android.widget.Toast
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

val YsDisplayMedium = FontFamily(
    Font(R.font.ys_display_medium)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = koinViewModel(),
//    onVacancyClick: (String) -> Unit = {}
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
                modifier = Modifier
                    .height(64.dp),
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
