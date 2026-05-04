package ru.practicum.android.diploma.presentation.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.ui.theme.ActiveBlue
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.FieldGray
import ru.practicum.android.diploma.presentation.ui.theme.FilterCheckboxSize
import ru.practicum.android.diploma.presentation.ui.theme.FilterClearIconSize
import ru.practicum.android.diploma.presentation.ui.theme.FilterCornerRadius
import ru.practicum.android.diploma.presentation.ui.theme.FilterFieldHeight
import ru.practicum.android.diploma.presentation.ui.theme.FilterHorizontalPadding
import ru.practicum.android.diploma.presentation.ui.theme.FilterIconSize
import ru.practicum.android.diploma.presentation.ui.theme.FilterResetRed
import ru.practicum.android.diploma.presentation.ui.theme.FilterSalaryFieldHeight
import ru.practicum.android.diploma.presentation.ui.theme.InactiveGray
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.ui.theme.TextSize16
import ru.practicum.android.diploma.presentation.ui.theme.TextSize12

val YsDisplayRegular = FontFamily(
    Font(R.font.ys_display_regular)
)

@Composable
fun FilterFieldRow(
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(FilterFieldHeight)
            .clickable { onClick() }
            .padding(start = FilterHorizontalPadding, end = FilterHorizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = placeholder,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = YsDisplayRegular,
            color = InactiveGray,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_filter_arrow),
            contentDescription = null,
            modifier = Modifier.size(FilterIconSize),
            tint = BlackPrimary
        )
    }
}

// Поле ЗП
@Composable
fun SalaryInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(FilterSalaryFieldHeight)
            .clip(RoundedCornerShape(FilterCornerRadius))
            .background(FieldGray)
            .clickable {
                focusRequester.requestFocus()
            }
            .padding(horizontal = FilterHorizontalPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.filter_expected_salary),
                fontSize = TextSize12,
                fontFamily = YsDisplayRegular,
                color = if (isFocused) ActiveBlue else InactiveGray,

            )

            BasicTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = TextSize16,
                    fontFamily = YsDisplayRegular,
                    color = BlackPrimary
                ),
                cursorBrush = SolidColor(ActiveBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.filter_salary_hint),
                                fontSize = TextSize16,
                                fontFamily = YsDisplayRegular,
                                color = InactiveGray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        if (value.isNotEmpty()) {
            Icon(
                painter = painterResource(R.drawable.ic_clear),
                contentDescription = stringResource(R.string.clear_text),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(FilterClearIconSize)
                    .clickable {
                        onClear()
                        focusManager.clearFocus()
                        isFocused = false
                    },
                tint = BlackPrimary
            )
        }
    }
}

// Не показывать без ЗП
@Composable
fun NoSalaryCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = PaddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.filter_do_not_show_without_salary),
            fontSize = TextSize16,
            fontFamily = YsDisplayRegular,
            color = BlackPrimary
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(FilterCheckboxSize),
            colors = CheckboxDefaults.colors(
                checkedColor = ActiveBlue,
                uncheckedColor = ActiveBlue,
                checkmarkColor = Color.White
            )
        )
    }
}

//Кнопки

// Кнопка "Применить"
@Composable
fun ApplyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(FilterFieldHeight),
        shape = RoundedCornerShape(FilterCornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = ActiveBlue,
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(
            text = stringResource(R.string.filter_apply),
            fontSize = TextSize16,
        )
    }
}

// Кнопка "Сбросить"
@Composable
fun ResetButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(FilterFieldHeight),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = FilterResetRed
        )
    ) {
        Text(
            text = stringResource(R.string.filter_reset),
            fontSize = TextSize16,
            color = FilterResetRed
        )
    }
}
