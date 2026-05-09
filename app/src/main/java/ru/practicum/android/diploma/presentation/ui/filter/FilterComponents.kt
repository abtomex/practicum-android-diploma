package ru.practicum.android.diploma.presentation.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.ui.search.YsDisplayMedium
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
import ru.practicum.android.diploma.presentation.ui.theme.TextSize12
import ru.practicum.android.diploma.presentation.ui.theme.TextSize16
import ru.practicum.android.diploma.presentation.ui.theme.WhiteBackground

val YsDisplayRegular = FontFamily(
    Font(R.font.ys_display_regular)
)

@Preview
@Composable
fun FilterFieldRowPreview_1() {
    FilterFieldRow(
        placeholder = "Отрасль",
        selectedItemText = "",
        onClick = {},
        onClearIconClick = {}

    )
}

@Preview
@Composable
fun FilterFieldRowPreview_2() {
    FilterFieldRow(
        placeholder = "Отрасль",
        selectedItemText = "Очень длинное название отрасли, которое должно занять несколько строчек",
        onClick = {},
        onClearIconClick = {}

    )
}

@Preview
@Composable
fun FilterFieldRowPreview_3() {
    FilterFieldRow(
        placeholder = "Отрасль",
        selectedItemText = "IT",
        onClick = {},
        onClearIconClick = {}

    )
}

@Composable
fun FilterFieldRow(
    placeholder: String,
    selectedItemText: String,
    onClick: () -> Unit,
    onClearIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .background(WhiteBackground)
            .fillMaxWidth()
            .heightIn(min = FilterFieldHeight)
            .clickable { onClick() }
            .padding(start = FilterHorizontalPadding, end = FilterHorizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedItemText.trim().isEmpty()) {
            Text(
                fontSize = 16.sp,
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = YsDisplayRegular,
                color = InactiveGray,
                modifier = Modifier.weight(1f)
            )
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = PaddingSmall)
            ) {
                Text(
                    fontSize = 12.sp,
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = YsDisplayRegular,
                    color = BlackPrimary,
                )
                Text(
                    text = selectedItemText,
                    fontFamily = YsDisplayRegular,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BlackPrimary,
                    lineHeight = 20.sp,

                )

            }
        }

        if (selectedItemText.trim().isEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_forward),
                contentDescription = null,
                modifier = Modifier.size(FilterIconSize),
                tint = BlackPrimary
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = null,
                modifier = Modifier
                    .size(FilterIconSize)
                    .clickable(onClick = onClearIconClick),
                tint = BlackPrimary
            )
        }
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
            .padding(horizontal = FilterHorizontalPadding)
    ) {
        SalaryInputContent(
            value = value,
            isFocused = isFocused,
            onValueChange = onValueChange,
            focusRequester = focusRequester,
            onFocusChanged = { isFocused = it }
        )

        if (value.isNotEmpty()) {
            ClearSalaryButton(onClear = {
                onClear()
                focusManager.clearFocus()
                isFocused = false
            })
        }
    }
}

@Composable
private fun SalaryInputContent(
    value: String,
    isFocused: Boolean,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        SalaryLabel(isFocused = isFocused)

        SalaryBasicTextField(
            value = value,
            onValueChange = onValueChange,
            focusRequester = focusRequester,
            onFocusChanged = onFocusChanged
        )
    }
}

@Composable
private fun SalaryLabel(isFocused: Boolean) {
    Text(
        text = stringResource(R.string.filter_expected_salary),
        fontSize = TextSize12,
        fontFamily = YsDisplayRegular,
        color = if (isFocused) ActiveBlue else InactiveGray,
    )
}

@Composable
private fun SalaryBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit
) {
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
                onFocusChanged(focusState.isFocused)
            },
        decorationBox = { innerTextField ->
            SalaryTextFieldDecoration(
                value = value,
                innerTextField = innerTextField
            )
        }
    )
}

@Composable
private fun SalaryTextFieldDecoration(
    value: String,
    innerTextField: @Composable () -> Unit
) {
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

@Composable
private fun BoxScope.ClearSalaryButton(
    onClear: () -> Unit
) {
    Icon(
        painter = painterResource(R.drawable.ic_clear),
        contentDescription = stringResource(R.string.clear_text),
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .size(FilterClearIconSize)
            .clickable { onClear() },
        tint = BlackPrimary
    )
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

// Кнопки

// Кнопка "Применить"
@Composable
fun ApplyButton(
    text: String,
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
            text = text,
            fontFamily = YsDisplayMedium,
            fontSize = 16.sp
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
