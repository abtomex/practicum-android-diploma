package ru.practicum.android.diploma.presentation.ui.search

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.components.VacancyCardItem

@Composable
fun SearchResultsContent(
    vacancyCards: List<VacancyCard>,
//    viewModel: SearchViewModel,
    loadNextPage: () -> Unit,
    navController: NavController
) {

    val lazyListState = rememberLazyListState()
    // Проверяем, достигнут ли конец списка
    // Следим за прокруткой и загружаем новые данные при достижении конца
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= vacancyCards.size - 1 &&
                    vacancyCards.isNotEmpty()) {
                    // Достигнут конец списка
//                    viewModel.loadNextPage()
                    loadNextPage.invoke()
                    Log.i("bazinga", "Достигнут конец страницы")
                }
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        state = lazyListState
    ) {
        items(vacancyCards, key = { it.id }) { vacancyCard ->
            VacancyCardItem(
                vacancy = vacancyCard,
                onItemClick = {
                    // TODO: реализовать переход к описанию вакансии
                },
                onFavoriteClick = {
                    //TODO:
                }
            )
        }
    }
}
