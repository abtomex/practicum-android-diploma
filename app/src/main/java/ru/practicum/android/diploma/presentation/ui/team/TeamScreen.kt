package ru.practicum.android.diploma.presentation.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.team_title),
                        fontSize = 22.sp,
                        color = BlackPrimary
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
                .padding(PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.team_title_label),
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.team_member_anna),
                modifier = Modifier.Companion.padding(top = PaddingSmall)
            )
            Text(text = stringResource(R.string.team_member_olga))
            Text(text = stringResource(R.string.team_member_veniamin))
            Text(text = stringResource(R.string.team_member_anton))
        }
    }
}
