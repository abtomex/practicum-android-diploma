package ru.practicum.android.diploma.presentation.ui.vacancydetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.presentation.ui.theme.BlackPrimary
import ru.practicum.android.diploma.presentation.ui.theme.CityGray
import ru.practicum.android.diploma.presentation.ui.theme.CommentGray
import ru.practicum.android.diploma.presentation.ui.theme.ContactBlue
import ru.practicum.android.diploma.presentation.ui.theme.CornerRadiusSmall
import ru.practicum.android.diploma.presentation.ui.theme.ErrorIconSize
import ru.practicum.android.diploma.presentation.ui.theme.FieldGray
import ru.practicum.android.diploma.presentation.ui.theme.LineHeight19
import ru.practicum.android.diploma.presentation.ui.theme.LineHeight26
import ru.practicum.android.diploma.presentation.ui.theme.LineHeight38
import ru.practicum.android.diploma.presentation.ui.theme.LogoSizeMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingMedium
import ru.practicum.android.diploma.presentation.ui.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.ui.theme.TextSize14
import ru.practicum.android.diploma.presentation.ui.theme.TextSize16
import ru.practicum.android.diploma.presentation.ui.theme.TextSize22
import ru.practicum.android.diploma.presentation.ui.theme.TextSize32
import ru.practicum.android.diploma.presentation.ui.theme.TitleSize18
import ru.practicum.android.diploma.presentation.ui.theme.TitleSize22
import ru.practicum.android.diploma.presentation.ui.theme.WhiteBackground
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsState
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel
import androidx.core.net.toUri

val YsDisplay = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_bold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailsScreen(
    vacancyId: String,
    viewModel: VacancyDetailsViewModel,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isFavorite by remember { mutableStateOf(false) }
    var currentVacancy by remember { mutableStateOf<VacancyDetails?>(null) }

    LaunchedEffect(vacancyId) {
        viewModel.loadVacancyDetails(vacancyId, fromNetwork = true)
        viewModel.checkIsFavorite(vacancyId) { favorite ->
            isFavorite = favorite
        }
    }

    LaunchedEffect(state) {
        if (state is VacancyDetailsState.Content) {
            currentVacancy = (state as VacancyDetailsState.Content).vacancy
        }
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.vacancy_title),
                        fontSize = TitleSize22,
                        fontWeight = FontWeight.Medium,
                        fontFamily = YsDisplay,
                        lineHeight = LineHeight26,
                        color = BlackPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = stringResource(R.string.back),
                            tint = BlackPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            currentVacancy?.let { vacancy ->
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, vacancy.url)
                                }
                                context.startActivity(Intent.createChooser(shareIntent, null))
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = stringResource(R.string.share),
                            tint = BlackPrimary
                        )
                    }
                    IconButton(
                        onClick = {
                            currentVacancy?.let { vacancy ->
                                viewModel.toggleFavorite(vacancy, isFavorite)
                                isFavorite = !isFavorite
                            }
                        },
                        enabled = currentVacancy != null
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
                            ),
                            contentDescription = stringResource(R.string.favorites_title),
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is VacancyDetailsState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is VacancyDetailsState.Content -> {
                    val vacancy = (state as VacancyDetailsState.Content).vacancy
                    VacancyDetailsContent(vacancy = vacancy)
                }
                is VacancyDetailsState.NoInternet -> {
                    ErrorPlaceholder(text = stringResource(R.string.no_connect))
                }
                is VacancyDetailsState.Error -> {
                    ErrorPlaceholder(text = stringResource(R.string.server_error_msg))
                }
            }
        }
    }
}

@Composable
private fun VacancyDetailsContent(vacancy: VacancyDetails) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PaddingMedium)
    ) {
        // НАЗВАНИЕ ВАКАНСИИ
        Text(
            text = vacancy.name,
            fontSize = TextSize32,
            fontWeight = FontWeight.Bold,
            fontFamily = YsDisplay,
            lineHeight = LineHeight38,
            color = BlackPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))

        // ЗАРПЛАТА
        Text(
            text = formatSalary(vacancy.salary?.from, vacancy.salary?.to, vacancy.salary?.currency),
            fontSize = TextSize22,
            fontWeight = FontWeight.Medium,
            fontFamily = YsDisplay,
            lineHeight = LineHeight26,
            color = BlackPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // ЛОГОТИП, КОМПАНИЯ, ГОРОД
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FieldGray, RoundedCornerShape(CornerRadiusSmall))
                .padding(PaddingMedium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = vacancy.employer.logo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(LogoSizeMedium)
                        .clip(RoundedCornerShape(CornerRadiusSmall))
                        .background(WhiteBackground),
                    error = painterResource(id = R.drawable.ic_company_placeholder),
                    placeholder = painterResource(id = R.drawable.ic_company_placeholder)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = vacancy.employer.name,
                        fontSize = TextSize22,
                        fontWeight = FontWeight.Medium,
                        fontFamily = YsDisplay,
                        lineHeight = LineHeight26,
                        color = BlackPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = vacancy.address?.city ?: vacancy.area.name,
                        fontSize = TextSize16,
                        fontWeight = FontWeight.Normal,
                        fontFamily = YsDisplay,
                        lineHeight = LineHeight19,
                        color = CityGray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // ТРЕБУЕМЫЙ ОПЫТ
        if (vacancy.experience != null) {
            Text(
                text = stringResource(R.string.required_experience),
                fontSize = TextSize16,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = LineHeight19,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = vacancy.experience.name,
                fontSize = TextSize16,
                fontWeight = FontWeight.Normal,
                fontFamily = YsDisplay,
                lineHeight = LineHeight19,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ГРАФИК И ЗАНЯТОСТЬ
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (vacancy.schedule != null) {
                Text(
                    text = vacancy.schedule.name,
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary
                )
            }
            if (vacancy.schedule != null && vacancy.employment != null) {
                Text(
                    text = ", ",
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary
                )
            }
            if (vacancy.employment != null) {
                Text(
                    text = vacancy.employment.name,
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // ОПИСАНИЕ ВАКАНСИИ (ЗАГОЛОВОК)
        Text(
            text = stringResource(R.string.vacancy_description),
            fontSize = TextSize22,
            fontWeight = FontWeight.Medium,
            fontFamily = YsDisplay,
            lineHeight = LineHeight26,
            color = BlackPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        val cleanText = vacancy.description
            .replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .trim()

        // ОБЯЗАННОСТИ
        val responsibilities = splitIntoSentences(cleanText, "Обязанности")
        if (responsibilities.isNotEmpty()) {
            Text(
                text = stringResource(R.string.responsibilities_header),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = 19.sp,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            responsibilities.forEach { sentence ->
                Text(
                    text = "• $sentence",
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary,
                    modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ТРЕБОВАНИЯ
        val requirements = splitIntoSentences(cleanText, "Требования")
        if (requirements.isNotEmpty()) {
            Text(
                text = stringResource(R.string.requirements_header),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = 19.sp,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            requirements.forEach { sentence ->
                Text(
                    text = "• $sentence",
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary,
                    modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // УСЛОВИЯ
        val conditions = splitIntoSentences(cleanText, "Условия")
        if (conditions.isNotEmpty()) {
            Text(
                text = stringResource(R.string.conditions_header),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = 19.sp,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            conditions.forEach { sentence ->
                Text(
                    text = "• $sentence",
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary,
                    modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // КЛЮЧЕВЫЕ НАВЫКИ
        if (vacancy.skills.isNotEmpty()) {
            Text(
                text = stringResource(R.string.key_skills),
                fontSize = TextSize22,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = LineHeight26,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            vacancy.skills.forEach { skill ->
                Text(
                    text = "• $skill",
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary,
                    modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // КОНТАКТЫ
        vacancy.contacts?.let { contacts ->
            Text(
                text = stringResource(R.string.contacts),
                fontSize = TextSize22,
                fontWeight = FontWeight.Medium,
                fontFamily = YsDisplay,
                lineHeight = LineHeight26,
                color = BlackPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (contacts.name.isNotBlank()) {
                Text(
                    text = contacts.name,
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = BlackPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (contacts.email.isNotBlank()) {
                Text(
                    text = contacts.email,
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = ContactBlue,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:${contacts.email}".toUri()
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            contacts.phones.forEach { phone ->
                Text(
                    text = phone.formatted,
                    fontSize = TextSize16,
                    fontWeight = FontWeight.Normal,
                    fontFamily = YsDisplay,
                    lineHeight = LineHeight19,
                    color = ContactBlue,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${phone.formatted.replace(Regex("[^0-9+]"), "")}")
                        }
                        context.startActivity(intent)
                    }
                )
                phone.comment?.let {
                    Text(
                        text = it,
                        fontSize = TextSize14,
                        fontWeight = FontWeight.Normal,
                        fontFamily = YsDisplay,
                        lineHeight = LineHeight19,
                        color = CommentGray,
                        modifier = Modifier.padding(start = PaddingSmall)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private fun splitIntoSentences(fullText: String, sectionName: String): List<String> {
    val startIndex = fullText.indexOf(sectionName)
    if (startIndex == -1) return emptyList()

    val nextSections = listOf("Обязанности", "Требования", "Условия")
    var endIndex = fullText.length
    for (nextSection in nextSections) {
        if (nextSection != sectionName) {
            val nextIndex = fullText.indexOf(nextSection, startIndex + sectionName.length)
            if (nextIndex != -1 && nextIndex < endIndex) {
                endIndex = nextIndex
            }
        }
    }

    val content = fullText.substring(startIndex + sectionName.length, endIndex).trim()
    if (content.isEmpty()) return emptyList()

    val result = mutableListOf<String>()
    var currentSentence = StringBuilder()

    for (char in content) {
        currentSentence.append(char)
        if (char == '.' || char == '!' || char == '?') {
            var sentence = currentSentence.toString().trim()
            if (sentence.isNotEmpty()) {
                sentence = sentence.replace(Regex("^[•\\-*\\d+.]\\s*"), "")
                sentence = sentence.replace(Regex("\\s+"), " ")
                if (sentence.isNotEmpty()) {
                    val firstChar = sentence[0].uppercaseChar()
                    val rest = if (sentence.length > 1) sentence.substring(1) else ""
                    sentence = firstChar + rest
                }
                result.add(sentence)
            }
            currentSentence = StringBuilder()
        }
    }

    return result
}

@Composable
private fun ErrorPlaceholder(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_no_connect),
            contentDescription = null,
            modifier = Modifier.size(ErrorIconSize),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            fontSize = TitleSize18,
            fontWeight = FontWeight.Medium,
            fontFamily = YsDisplay,
            lineHeight = LineHeight26,
            color = BlackPrimary
        )
    }
}

private fun formatSalary(from: Int?, to: Int?, currency: String?): String {
    val currencySymbol = when (currency) {
        "RUR", "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        "BYR" -> "Br"
        "UZS" -> "сўм"
        "UAH" -> "₴"
        "AZN" -> "₼"
        "GEL" -> "₾"
        "KZT" -> "₸"
        else -> currency ?: ""
    }

    return when {
        from != null && to != null -> "от $from до $to $currencySymbol"
        from != null -> "от $from $currencySymbol"
        to != null -> "до $to $currencySymbol"
        else -> "Уровень зарплаты не указан"
    }
}
