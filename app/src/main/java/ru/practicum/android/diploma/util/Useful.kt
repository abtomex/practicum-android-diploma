package ru.practicum.android.diploma.util

import android.icu.text.DecimalFormat

object Useful {

    private const val GRP_SIZE = 3

    fun formatNumberWithSpaces(number: Int): String {
        val formatter = DecimalFormat("#,###").apply {
            groupingSize = GRP_SIZE
        }
        return formatter.format(number).replace(',', ' ')
    }

}
