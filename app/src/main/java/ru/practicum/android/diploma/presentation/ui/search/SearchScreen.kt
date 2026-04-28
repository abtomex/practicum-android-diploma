package ru.practicum.android.diploma.presentation.ui.search

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.IconSizeDefault
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel
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
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var currentPage by remember { mutableIntStateOf(1) }
    val loadedData = remember { mutableStateListOf<VacancyCard>() }

    val focusManager = LocalFocusManager.current
    val state by viewModel.getState().observeAsState(initial = SearchState.Content(emptyList(), 1, 1))

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(PaddingMedium)
        ) {
            SearchField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    val trimmedText = newText.text.trim()
                    if (trimmedText.length > 2 || trimmedText.isEmpty()) {
                        viewModel.onSearchQueryChanged(trimmedText)
                    }
                },
                onClear = {
                    searchText = TextFieldValue("")
                    loadedData.clear()
                    currentPage = 1
                    focusManager.clearFocus()
                },
                onSearch = {
                    focusManager.clearFocus()
                    val trimmedText = searchText.text.trim()
                    if (trimmedText.length > 2) {
                        currentPage = 1
                        loadedData.clear()
                        viewModel.loadData(trimmedText, 1)
                    }
                },
                onTapSearch = {
                    // Можно использовать для загрузки истории поиска
                }
            )

            when (val currentState = state) {
                is SearchState.Loading -> LoadingContent()
                is SearchState.Error -> ErrorContent(
                    message = currentState.message,
                )
                is SearchState.NoInternet -> NoInternetContent()
                is SearchState.Content -> {
                    if (currentState.data.isEmpty()) {
                        EmptyContent()
                    } else {
                        // Первая загрузка или новый поиск
                        if (currentState.page == 1) {
                            loadedData.clear()
                            loadedData.addAll(currentState.data)
                            currentPage = currentState.page
                        }

                        SearchResultsContent(
                            vacancyCards = loadedData,
                            currentPage = currentPage,
                            totalPages = currentState.totalPages,
                            loadNextPage = {
                                if (currentPage < currentState.totalPages) {
                                    viewModel.loadNextPage(currentPage + 1)
                                }
                            },
                            loadPreviousPage = {
                                if (currentPage > 1) {
                                    viewModel.loadPreviousPage(currentPage - 1)
                                }
                            },
                            navController = navController
                        )
                    }
                }
                is SearchState.ContentNextPage -> {
                    if (currentState.data.isNotEmpty()) {
                        // Добавляем новую страницу в правильной позиции
                        if (currentState.page > currentPage) {
                            // Добавляем в конец (следующая страница)
                            val newItems = currentState.data.filter { newItem ->
                                !loadedData.any { it.id == newItem.id }
                            }
                            loadedData.addAll(newItems)
                        } else if (currentState.page < currentPage) {
                            // Добавляем в начало (предыдущая страница)
                            val newItems = currentState.data.filter { newItem ->
                                !loadedData.any { it.id == newItem.id }
                            }
                            loadedData.addAll(0, newItems)
                        }
                        currentPage = currentState.page

                        SearchResultsContent(
                            vacancyCards = loadedData,
                            currentPage = currentPage,
                            totalPages = currentState.totalPages,
                            loadNextPage = {
                                if (currentPage < currentState.totalPages) {
                                    viewModel.loadNextPage(currentPage + 1)
                                }
                            },
                            loadPreviousPage = {
                                if (currentPage > 1) {
                                    viewModel.loadPreviousPage(currentPage - 1)
                                }
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
