package ru.practicum.android.diploma.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.ui.search.YsDisplayMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarBackButton(
    title: String,
    navController: NavHostController
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = YsDisplayMedium,
                fontSize = 22.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(painter = painterResource(R.drawable.ic_back), contentDescription = "Назад")
            }
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}
