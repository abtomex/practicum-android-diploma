package ru.practicum.android.diploma.presentation.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.util.Useful.Companion.formatNumberWithSpaces

@Composable
fun VacancyCardContent(
    vacancy: VacancyCard,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        EmployerLogo(
            logoUrl = vacancy.logo,
            employerName = vacancy.company ?: ""
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${vacancy.name}, ${vacancy.city}",
                style = MaterialTheme.typography.titleSmall,
                color = BlackPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = vacancy.company ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = BlackPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatSalary(vacancy),
                style = MaterialTheme.typography.titleSmall,
                color = BlackPrimary
            )
        }
    }
}

@Composable
fun formatSalary(vacancy: VacancyCard): String {
    val from = vacancy.salary?.from
    val to = vacancy.salary?.to
    val currency = symbol(vacancy.salary?.currency)

    return when {
        from != null && to != null -> stringResource(
            R.string.salary_range,
            formatNumberWithSpaces(from),
            formatNumberWithSpaces(to),
            currency
        )

        from != null -> stringResource(
            R.string.salary_from,
            formatNumberWithSpaces(from),
            currency
        )

        to != null -> stringResource(
            R.string.salary_to,
            formatNumberWithSpaces(to),
            currency
        )

        else -> stringResource(R.string.not_specified)
    }
}

fun symbol(currency: String?): String {
    return when (currency) {
        "RUR", "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        "BYR" -> "Br"
        "UZS" -> "сўм"
        "UAH" -> "₴"
        "AZN" -> "₼"
        "GEL" -> "₾"
        "KZT" -> "₸"
        else -> currency ?: ""
    }
}

