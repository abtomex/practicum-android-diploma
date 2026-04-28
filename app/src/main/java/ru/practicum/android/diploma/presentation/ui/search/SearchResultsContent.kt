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
    currentPage: Int,
    totalPages: Int,
    loadNextPage: () -> Unit,
    loadPreviousPage: () -> Unit,
    navController: NavController
) {
    val lazyListState = rememberLazyListState()

    // Отслеживаем достижение конца списка (для загрузки следующей страницы)
    LaunchedEffect(lazyListState, vacancyCards.size) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= vacancyCards.size - 1 &&
                    vacancyCards.isNotEmpty() &&
                    currentPage < totalPages) {
                    Log.i("Pagination", "Загружаем следующую страницу ${currentPage + 1}")
                    loadNextPage()
                }
            }
    }

    // Отслеживаем достижение начала списка (для загрузки предыдущей страницы)
    LaunchedEffect(lazyListState, vacancyCards.size) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index }
            .distinctUntilChanged()
            .collect { firstVisibleIndex ->
                if (firstVisibleIndex != null &&
                    firstVisibleIndex <= 0 &&
                    vacancyCards.isNotEmpty() &&
                    currentPage > 1) {
                    Log.i("Pagination", "Загружаем предыдущую страницу ${currentPage - 1}")
                    // Сохраняем текущую позицию прокрутки перед загрузкой
                    val currentScrollPosition = lazyListState.firstVisibleItemIndex
                    loadPreviousPage()
                    // Восстанавливаем позицию после загрузки (нужно добавить callback)
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
                    // TODO:
                }
            )
        }
    }
}
