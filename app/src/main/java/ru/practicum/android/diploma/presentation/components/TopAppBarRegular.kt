package ru.practicum.android.diploma.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.presentation.ui.search.YsDisplayMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarRegular(
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = YsDisplayMedium,
                fontSize = 22.sp
            )
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}
