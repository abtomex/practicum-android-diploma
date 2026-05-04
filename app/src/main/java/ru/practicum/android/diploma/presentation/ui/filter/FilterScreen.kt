package ru.practicum.android.diploma.presentation.ui.filter

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.FilterSpacerLarge
import ru.practicum.android.diploma.presentation.ui.theme.FilterSpacerMedium
import ru.practicum.android.diploma.presentation.ui.theme.FilterSpacerSmall
import ru.practicum.android.diploma.presentation.ui.theme.FilterTopBarFontSize
import ru.practicum.android.diploma.presentation.ui.theme.FilterTopBarTopPadding
import ru.practicum.android.diploma.presentation.ui.theme.IconSizeDefault
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.viewmodel.FiltersScreenViewModel

val YsDisplayMediumFilter = FontFamily(
    Font(R.font.ys_display_medium)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavHostController,
    viewModel: FiltersScreenViewModel
) {
    val salaryInput by viewModel.salaryInput.collectAsStateWithLifecycle()
    val selectedIndustryName by viewModel.selectedIndustryName.collectAsStateWithLifecycle()
    val hideWithoutSalary by viewModel.hideWithoutSalary.collectAsStateWithLifecycle()
    val hasActiveFilters by viewModel.hasActiveFilters.collectAsStateWithLifecycle()

    // Получение данных со следующего экрана при возврате (отрасль)
    val backStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(backStackEntry) {
        val currentEntry = backStackEntry
        currentEntry?.savedStateHandle?.getLiveData<Int>("industry_id")
            ?.observeForever { industryId ->
                if (industryId != null && industryId != 0) {

                    val entry = backStackEntry
                    val industryName = entry?.savedStateHandle
                        ?.get<String>("industry_name") ?: ""
                    viewModel.updateSelectedIndustry(industryId, industryName)
                    // Очищаем после получения
                    entry?.savedStateHandle?.remove<Int>("industry_id")
                    entry?.savedStateHandle?.remove<String>("industry_name")
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.filter_title),
                        fontSize = FilterTopBarFontSize,
                        color = BlackPrimary,
                        fontFamily = YsDisplayMediumFilter
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = stringResource(R.string.back),
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
                .padding(top = FilterTopBarTopPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Поле "Место работы"
                FilterFieldRow(
                    placeholder = stringResource(R.string.filter_place),
                    onClick = { //ЗАГЛУШКА
                    },

                )

                // Поле "Отрасль"
                FilterFieldRow(
                    placeholder = selectedIndustryName.ifEmpty {
                        stringResource(R.string.filter_industry)
                    },
                    onClick = {
                        navController.navigate(Destination.IndustryFilter.route)
                    }
                )

                Spacer(modifier = Modifier.height(FilterSpacerMedium))

                // Поле ввода зарплаты
                SalaryInputField(
                    value = salaryInput,
                    onValueChange = { viewModel.updateSalaryInput(it) },
                    onClear = { viewModel.updateSalaryInput("") },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )

                Spacer(modifier = Modifier.height(FilterSpacerMedium))

                // Чекбокс
                NoSalaryCheckbox(
                    checked = hideWithoutSalary,
                    onCheckedChange = { viewModel.updateHideWithoutSalary(it) },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )

                Spacer(modifier = Modifier.height(FilterSpacerLarge))
            }

            // Кнопки
            if (hasActiveFilters) {
                ApplyButton(
                    onClick = {
                        viewModel.applyFilters()
                        navController.navigateUp()
                    },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )

                Spacer(modifier = Modifier.height(FilterSpacerSmall))

                ResetButton(
                    onClick = {
                        viewModel.resetFilters()
                    },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )
            }
        }
    }
}
