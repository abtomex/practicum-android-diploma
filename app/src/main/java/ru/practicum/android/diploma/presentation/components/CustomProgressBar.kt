package ru.practicum.android.diploma.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.presentation.ui.theme.ActiveBlue

@Composable
fun CustomProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 124.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(44.dp)
                .align(alignment = Alignment.TopCenter),
            color = ActiveBlue
        )
    }
}
