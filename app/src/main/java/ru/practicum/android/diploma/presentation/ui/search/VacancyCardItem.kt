package ru.practicum.android.diploma.presentation.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.Useful.Companion.dpToPx

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VacancyCardItem(
    vacancyCard: VacancyCard,
    viewModel: SearchViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
//                if (viewModel.clickIsAllowed) {
//                    viewModel.clickIsAllowed = false
//                    viewModel.clickDebounce()
//                    viewModel.addToHistory(vacancyCard)
//
//                    navController.navigate(
//                        R.id.action_searchFragment_to_audioPlayerFragment,
//                        AudioPlayerFragment.createArgs(viewModel.gson()?.toJson(vacancyCard))
//                    )
//                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
//            model = "https://upload.wikimedia.org/wikipedia/commons/      f/fa/Apple_logo_black.svg",
              model = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/120px-Apple_logo_black.svg.png",
//            model = ImageRequest.Builder(LocalContext.current)
//                .data("https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg")
//                .crossfade(true)
//                .build(),
            contentDescription = vacancyCard.name,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_no_logo_placeholder_45),
            failure = placeholder(R.drawable.ic_no_logo_placeholder_45)
        )
        {
            it.transform(RoundedCorners(dpToPx(2.0f, context = context)))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = vacancyCard.name,
                fontSize = 16.sp,
                maxLines = 1,
                color = BlackPrimary,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (vacancyCard.company != null) Text(
                    text = vacancyCard.company,
                    fontSize = 16.sp,
                    color = BlackPrimary,
                    maxLines = 1,
                )

//                Text(
//                    text = "•",
//                    fontSize = 14.sp,
//                    color = colorResource(R.color.track_text_artist_color),
//                )
                if (vacancyCard.salary != null) Text(
                    text = "${vacancyCard.salary.to} ${vacancyCard.salary.currency}",
                    fontSize = 16.sp,
                    color = BlackPrimary,
                    maxLines = 1,
                )

//                Text(text = vacancyCard.salary,
//                    fontSize = 16.sp,
//                    color = BlackPrimary,
//                )
            }
        }

    }

}
