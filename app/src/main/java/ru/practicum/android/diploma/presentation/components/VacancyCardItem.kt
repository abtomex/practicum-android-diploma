package ru.practicum.android.diploma.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.LogoSizeMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall
import ru.practicum.android.diploma.util.formatSalaryString

// Шрифт YS Display (аналогично странице Вакансия)
val YsDisplay = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_bold, FontWeight.Bold)
)

@Composable
fun VacancyCardItem(
    vacancy: VacancyCard,
    onItemClick: (String) -> Unit,
    onFavoriteClick: ((String) -> Unit)? = null,
    showFavoriteButton: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(vacancy.id) }
            .padding(PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Логотип компании — размер 48x48
        AsyncImage(
            model = vacancy.logo,
            contentDescription = null,
            modifier = Modifier.size(LogoSizeMedium),
            error = painterResource(id = R.drawable.ic_company_placeholder),
            placeholder = painterResource(id = R.drawable.ic_company_placeholder)
        )

        Spacer(modifier = Modifier.width(PaddingSmall))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Название вакансии и город — 22px Medium
            Text(
                text = "${vacancy.name}, ${vacancy.city ?: ""}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = 26.sp,
                color = BlackPrimary
            )

            // Название компании — 16px Regular
            Text(
                text = vacancy.company ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = YsDisplay,
                lineHeight = 19.sp,
                color = BlackPrimary,
            )

            // Зарплата — 16px Regular (чёрный цвет)
            Text(
                text = formatSalaryString(vacancy.salary?.from, vacancy.salary?.to, vacancy.salary?.currency),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = YsDisplay,
                lineHeight = 19.sp,
                color = BlackPrimary,
            )
        }

        // Кнопка избранного (только если showFavoriteButton = true)
        if (showFavoriteButton && onFavoriteClick != null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = "Удалить из избранного",
                modifier = Modifier
                    .size(LogoSizeMedium)
                    .clickable { onFavoriteClick.invoke(vacancy.id) },
                tint = BlackPrimary
            )
        }
    }
}
