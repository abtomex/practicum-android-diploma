package ru.practicum.android.diploma.presentation.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.components.TopAppBarRegular
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen() {
    Scaffold(
        topBar = {
            TopAppBarRegular(stringResource(R.string.team_title))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.team_title_label),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp,
                color = BlackPrimary
            )
            Text(
                text = stringResource(R.string.team_member_dmitriych),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 19.sp,
                color = BlackPrimary,
                modifier = Modifier.padding(top = PaddingSmall)
            )
            Text(
                text = stringResource(R.string.team_member_dmitriytr),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 19.sp,
                color = BlackPrimary,
                modifier = Modifier.padding(top = PaddingSmall)
            )
            Text(
                text = stringResource(R.string.team_member_vladimir),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 19.sp,
                color = BlackPrimary,
                modifier = Modifier.padding(top = PaddingSmall)
            )
            Text(
                text = stringResource(R.string.team_member_aleksandr),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 19.sp,
                color = BlackPrimary,
                modifier = Modifier.padding(top = PaddingSmall)
            )
            Text(
                text = stringResource(R.string.team_member_alina),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 19.sp,
                color = BlackPrimary,
                modifier = Modifier.padding(top = PaddingSmall)
            )
        }
    }
}
