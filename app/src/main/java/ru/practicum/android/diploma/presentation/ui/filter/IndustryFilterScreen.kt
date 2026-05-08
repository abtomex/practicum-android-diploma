package ru.practicum.android.diploma.presentation.ui.filter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.ui.search.DefaultContent
import ru.practicum.android.diploma.presentation.ui.search.EmptyContent
import ru.practicum.android.diploma.presentation.ui.search.ErrorContent
import ru.practicum.android.diploma.presentation.ui.search.LoadingContent
import ru.practicum.android.diploma.presentation.ui.search.NoInternetContent
import ru.practicum.android.diploma.presentation.ui.search.YsDisplayMedium
import ru.practicum.android.diploma.presentation.ui.theme.ActiveBlue
import ru.practicum.android.diploma.presentation.ui.theme.WhiteUniversal
import ru.practicum.android.diploma.presentation.viewmodel.IndustryFiltersViewModel
import ru.practicum.android.diploma.presentation.viewmodel.state.IndustryFiltersState


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun IndustrySelectionScreenPreview() {
    val mockInteractor = MockIndustriesInteractor()
    val testViewModel = IndustryFiltersViewModel(
        industryInteractor = mockInteractor
    )

    IndustrySelectionScreen(
        navController = rememberNavController(),
        viewModel = testViewModel
    )
}

private class MockIndustriesInteractor : IndustriesInteractor {
    override suspend fun getIndustriesList(): Flow<ApiResponse<List<Industry>?>> {
        return flowOf(ApiResponse.Success(emptyList()))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustrySelectionScreen(
    navController: NavHostController,
    viewModel: IndustryFiltersViewModel,
    previousSelectedId: Int = 0
) {
    val industriesState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getIndustriesList()
    }

    val industries = (industriesState as? IndustryFiltersState.Content)?.data

    var searchQuery by remember { mutableStateOf("") }
    var selectedIndustry by remember { mutableStateOf<Industry?>(null) }

    // Фильтрация списка отраслей в соответствии с поисковым запросом
    val searchedIndustries by remember(searchQuery, industries) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                industries
            } else {
                industries?.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    // Выбор ранее отмеченной отрасли при повторном заходе на экран
    LaunchedEffect(searchedIndustries) {
        if (previousSelectedId != 0) {
            selectedIndustry = searchedIndustries?.firstOrNull { it.id == previousSelectedId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.filter_industry_header),
                        fontFamily = YsDisplayMedium,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(R.drawable.ic_back), contentDescription = "Назад")
                    }
                }
            )
        },
        bottomBar = {
            // Показывать только при выбранной отрасли
            if (selectedIndustry != null) {
                // Контейнер для кнопки с отступами
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Button(
                        onClick = {
//                            viewModel.confirmFilter()

                            // Получаем доступ к состоянию предыдущего экрана
                            val navHandle = navController.previousBackStackEntry?.savedStateHandle

                            navHandle?.set("industry_id", selectedIndustry?.id)
                            navHandle?.set("industry_name", selectedIndustry?.name)

                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActiveBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.filter_industry_apply),
                            fontFamily = YsDisplayMedium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Поле поиска
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(stringResource(R.string.filter_industry_search), color = Color.Gray) },
                leadingIcon = { Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhiteUniversal,
                    unfocusedContainerColor = WhiteUniversal,
                    disabledContainerColor = WhiteUniversal,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            when (industriesState) {
                is IndustryFiltersState.Default -> DefaultContent()
                is IndustryFiltersState.Loading -> LoadingContent()
                is IndustryFiltersState.Content -> IndustriesContent(
                    searchedIndustries,
                    selectedIndustry,
                    onSelect = {
                        selectedIndustry = it
                        viewModel.addFilter(it)
                    }
                )

                is IndustryFiltersState.Empty -> EmptyContent()
                is IndustryFiltersState.NoInternet -> NoInternetContent()
                is IndustryFiltersState.Error -> ErrorContent()

            }
        }
    }
}

@Composable
fun IndustriesContent(
    searchedIndustries: List<Industry>?,
    selectedIndustry: Industry?,
    onSelect: (industry: Industry) -> Unit
) {
    // Список отраслей
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(searchedIndustries ?: emptyList()) { industry ->
            IndustryItem(
                industry = industry,
                isSelected = industry.id == selectedIndustry?.id,
                onSelect = { onSelect(industry) }
            )
        }
    }

}

@Composable
fun IndustryItem(
    industry: Industry,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = industry.name,
                modifier = Modifier.weight(1f),
                fontFamily = YsDisplayRegular,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(12.dp))

            RadioButton(
                selected = isSelected,
                onClick = null, // Клик обрабатывается в Surface
                colors = RadioButtonDefaults.colors(
                    selectedColor = ActiveBlue,
                    unselectedColor = ActiveBlue
                )
            )
        }
    }
}
