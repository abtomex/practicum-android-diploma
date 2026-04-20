package ru.practicum.android.diploma.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.navigation.Screen
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.IconSizeDefault
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.search_title),
                        fontSize = 22.sp,
                        color = BlackPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Filter.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier.size(IconSizeDefault),
                            tint = BlackPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.search_hint),
                fontSize = 16.sp
            )
        }
    }
}
