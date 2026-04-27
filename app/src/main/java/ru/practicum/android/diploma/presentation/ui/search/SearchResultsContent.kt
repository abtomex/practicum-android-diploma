package ru.practicum.android.diploma.presentation.ui.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel

@Composable
fun SearchResultsContent(
    vacancyCards: List<VacancyCard>,
    viewModel: SearchViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(vacancyCards, key = { it.id }) { vacancyCard ->
            VacancyCardItem(
                vacancyCard = vacancyCard,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
