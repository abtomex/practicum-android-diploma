package ru.practicum.android.diploma.presentation.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState

@Composable
fun SearchScreenContent(
    modifier: Modifier,
    state: SearchState = SearchState.Default,
    searchQuery: String = "",
    onQueryChanged: (String) -> Unit = {},
    onClearSearch: () -> Unit = {},
    onLoadNextPage: () -> Unit = {},
    onVacancyClick: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchField(
            searchStr = searchQuery,
            onValueChange = onQueryChanged,
            onClear = onClearSearch
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is SearchState.Default -> DefaultContent()
            is SearchState.Loading -> LoadingContent()
            is SearchState.Content -> SearchResultsContent(
                vacancyCards = state.data,
                found = state.found,
                isNextPageLoading = state.isNextPageLoading,
                onVacancyClick = onVacancyClick,
                onLoadNextPage = onLoadNextPage
            )

            is SearchState.Empty -> EmptyContent()
            is SearchState.NoInternet -> NoInternetContent()
            is SearchState.Error -> ErrorContent()
        }
    }
}
