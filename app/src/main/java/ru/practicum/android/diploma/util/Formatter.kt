package ru.practicum.android.diploma.util

fun formatSalaryString(from: Int?, to: Int?, currency: String?): String {
    val currencySymbol = when (currency) {
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

    return when {
        from != null && to != null -> "от $from до $to $currencySymbol"
        from != null -> "от $from $currencySymbol"
        to != null -> "до $to $currencySymbol"
        else -> "Уровень зарплаты не указан"
    }
}
