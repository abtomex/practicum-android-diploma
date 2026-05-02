package ru.practicum.android.diploma.presentation.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.ui.theme.InactiveGray

@Composable
fun EmployerLogo(
    logoUrl: String?,
    employerName: String
) {
    var isError by remember { mutableStateOf(false) }
    var isLoaded by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(12.dp)

    if (!logoUrl.isNullOrEmpty() && !isError) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(logoUrl)
                .crossfade(true)
                .addHeader("User-Agent", "Mozilla/5.0")
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = employerName,
            modifier = Modifier
                .size(48.dp)
                .clip(shape)
                .background(Color.White),
            contentScale = ContentScale.Fit,
            onError = {
                isError = true
            },
            onSuccess = {
                isLoaded = true
            }
        )
    } else {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = InactiveGray,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_company_placeholder),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
        }
    }
}
