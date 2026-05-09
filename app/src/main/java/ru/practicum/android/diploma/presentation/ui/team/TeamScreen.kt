package ru.practicum.android.diploma.presentation.ui.team

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.components.TopAppBarRegular
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall


@Preview
@Composable
fun TeamScreenPreview(){
    TeamScreen()
}

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
            TeamParticipant(
                participantText = stringResource(R.string.team_member_dmitriych),
                modifier = Modifier.height(24.dp),

            )
            TeamParticipant(stringResource(R.string.team_member_dmitriytr))
            TeamParticipant(stringResource(R.string.team_member_vladimir))
            TeamParticipant(stringResource(R.string.team_member_aleksandr))
            TeamParticipant(stringResource(R.string.team_member_alina))
        }
    }
}

@Composable
fun TeamParticipant(
    participantText: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.height(16.dp)
) {
    Spacer(modifier)
    Text(
        text = participantText,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 19.sp,
        color = BlackPrimary,
        modifier = Modifier.padding(top = PaddingSmall)
    )

}
