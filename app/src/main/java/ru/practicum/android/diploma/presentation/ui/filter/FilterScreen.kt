package ru.practicum.android.diploma.presentation.ui.filter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.components.TopAppBarBackButton
import ru.practicum.android.diploma.presentation.navigation.Destination
import ru.practicum.android.diploma.presentation.ui.theme.FilterSpacerLarge
import ru.practicum.android.diploma.presentation.ui.theme.FilterSpacerMedium
import ru.practicum.android.diploma.presentation.ui.theme.FilterSpacerSmall
import ru.practicum.android.diploma.presentation.ui.theme.FilterTopBarTopPadding
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.viewmodel.FiltersScreenViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun FilterScreenPreview() {
    FilterScreen(navController = rememberNavController(), viewModel = FiltersScreenViewModel())
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavHostController,
    viewModel: FiltersScreenViewModel
) {
    val salaryInput by viewModel.salaryInput.collectAsStateWithLifecycle()
    val selectedIndustryName by viewModel.selectedIndustryName.collectAsStateWithLifecycle()
    val selectedIndustryId by viewModel.selectedIndustryId.collectAsStateWithLifecycle()
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
            TopAppBarBackButton(
                title = stringResource(R.string.filter_title),
                navController = navController
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
                    selectedItemText = "",
                    onClick = { },
                    onClearIconClick = { },

                )

                // Поле "Отрасль"
                FilterFieldRow(
                    placeholder = stringResource(R.string.filter_industry),
                    selectedItemText = selectedIndustryName,
                    onClick = {
                        navController.navigate(Destination.IndustryFilter.createRoute(selectedIndustryId))
                    },
                    onClearIconClick = { viewModel.resetIndustry() }
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
                    text = stringResource(R.string.filter_apply),
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
