package ru.practicum.android.diploma.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.IconSizeDefault
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall

@Composable
fun VacancyCardItem(
    vacancy: VacancyCard,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(vacancy.id) }
            .padding(PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка компании
        AsyncImage(
            model = vacancy.logo,
            contentDescription = null,
            modifier = Modifier.size(IconSizeDefault),
            error = painterResource(id = R.drawable.ic_company_placeholder),
            placeholder = painterResource(id = R.drawable.ic_company_placeholder)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = PaddingSmall)
        ) {
            //информация о компании
            Text(
                text = "${vacancy.name}, ${vacancy.city ?: ""}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 26.sp,
                color = BlackPrimary
            )
            Text(
                text = vacancy.company ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 19.sp,
                color = BlackPrimary
            )
            Text(
                text = formatSalary(vacancy.salary?.from, vacancy.salary?.to, vacancy.salary?.currency),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 19.sp,
                color = BlackPrimary
            )
        }

        // Кнопка удаления из избранного
        Icon(
            painter = painterResource(id = R.drawable.ic_favorite),
            contentDescription = stringResource(R.string.remove_from_favorites),
            modifier = Modifier
                .size(IconSizeDefault)
                .clickable { onFavoriteClick(vacancy.id) },
            tint = BlackPrimary
        )
    }
}

private fun formatSalary(from: Int?, to: Int?, currency: String?): String {
    return when {
        from != null && to != null -> "от $from до $to ${currency ?: ""}"
        from != null -> "от $from ${currency ?: ""}"
        to != null -> "до $to ${currency ?: ""}"
        else -> "Уровень зарплаты не указан"
    }
}
