package ru.practicum.android.diploma.presentation.ui.filter

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
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
    viewModel: FiltersScreenViewModel = koinViewModel()
) {

    // Заглушка "Отрасль"
    var selectedIndustry by remember { mutableStateOf("") }

    // Поле ЗП
    var salaryInput by remember { mutableStateOf("") }

    // Чекбокс "Не показывать без зарплаты"
    var hideWithoutSalary by remember { mutableStateOf(false) }

    // Флаг для отображения кнопок
    val hasActiveFilters = remember(salaryInput, selectedIndustry, hideWithoutSalary) {
        salaryInput.isNotBlank() || selectedIndustry.isNotBlank() || hideWithoutSalary
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
                    placeholder = stringResource(R.string.filter_industry),
                    onClick = {
                        navController.navigate(Destination.IndustryFilter.route)
                     },

                )

                Spacer(modifier = Modifier.height(FilterSpacerMedium))

                // Поле ввода зарплаты
                SalaryInputField(
                    value = salaryInput,
                    onValueChange = { salaryInput = it },
                    onClear = { salaryInput = "" },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )

                Spacer(modifier = Modifier.height(FilterSpacerMedium))

                // Чекбокс
                NoSalaryCheckbox(
                    checked = hideWithoutSalary,
                    onCheckedChange = { hideWithoutSalary = it },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )

                Spacer(modifier = Modifier.height(FilterSpacerLarge))
            }

            // Кнопки
            if (hasActiveFilters) {
                ApplyButton(
                    onClick = {
                        // ЗАГЛУШКА
                        navController.navigateUp()
                        viewModel.applyFilters(salaryInput, selectedIndustry, hideWithoutSalary)

                    },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )

                Spacer(modifier = Modifier.height(FilterSpacerSmall))

                ResetButton(
                    onClick = {
                        salaryInput = ""
                        selectedIndustry = ""
                        hideWithoutSalary = false
                    },
                    modifier = Modifier.padding(horizontal = PaddingMedium)
                )
            }
        }
    }
}
