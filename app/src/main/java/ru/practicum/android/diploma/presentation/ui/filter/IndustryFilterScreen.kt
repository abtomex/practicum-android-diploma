package ru.practicum.android.diploma.presentation.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.ui.theme.ActiveBlue
import ru.practicum.android.diploma.presentation.ui.theme.WhiteUniversal
import ru.practicum.android.diploma.presentation.viewmodel.IndustryFiltersViewModel
import ru.practicum.android.diploma.presentation.viewmodel.state.IndustryFiltersState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustrySelectionScreen(
    navController: NavHostController,
    viewModel: IndustryFiltersViewModel
) {

    val industriesState by viewModel.state.collectAsState()

    viewModel.getIndustriesList()

    val industries = (industriesState as? IndustryFiltersState.Content)?.data

    var searchQuery by remember { mutableStateOf("") }
    var selectedIndustry by remember { mutableStateOf<Industry?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Выбор отрасли",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
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
            // Контейнер для кнопки с отступами
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.confirmFilter()

                        // 1. Получаем доступ к состоянию предыдущего экрана
                        val navHandle = navController.previousBackStackEntry?.savedStateHandle

                        navHandle?.set("industry_id", selectedIndustry?.id)
                        navHandle?.set("industry_name", selectedIndustry?.name)

                        // 2. Возвращаемся назад
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
                        text = "Выбрать",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
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
                placeholder = { Text("Введите отрасль", color = Color.Gray) },
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

            // Список отраслей
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(industries ?: emptyList()) { industry ->
                    IndustryItem(
                        industry = industry,
                        isSelected = industry.id == selectedIndustry?.id,
                        onSelect = {
                            selectedIndustry = industry
                            viewModel.addFilter(industry)
                        }
                    )
                }
            }
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
                fontSize = 15.sp,
                lineHeight = 20.sp,
                color = Color.Black
            )

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

