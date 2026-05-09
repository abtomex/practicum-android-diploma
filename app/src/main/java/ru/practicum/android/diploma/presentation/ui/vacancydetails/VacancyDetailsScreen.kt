package ru.practicum.android.diploma.presentation.ui.vacancydetails

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.core.net.toUri
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

val YsDisplay = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_bold, FontWeight.Bold)
)
private const val BULLET_POINT = "• "

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailsScreen(
    vacancyId: String,
    viewModel: VacancyDetailsViewModel,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val favoriteState = rememberFavoriteState(vacancyId, viewModel)
    val currentVacancy by rememberCurrentVacancy(state)

    Scaffold(
        topBar = {
            VacancyDetailsTopBar(
                currentVacancy = currentVacancy,
                isFavorite = favoriteState.isFavorite,
                onBackPressed = onBackPressed,
                onShareClick = { vacancy ->
                    createShareIntent(vacancy)
                },
                onFavoriteClick = { vacancy, isFav ->
                    viewModel.toggleFavorite(vacancy, isFav)
                    favoriteState.toggle()
                }
            )
        }
    ) { innerPadding ->
        VacancyDetailsContentWrapper(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state
        )
    }
}

@Composable
private fun rememberFavoriteState(
    vacancyId: String,
    viewModel: VacancyDetailsViewModel
): FavoriteState {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(vacancyId) {
        viewModel.loadVacancyDetails(vacancyId, fromNetwork = true)
        viewModel.checkIsFavorite(vacancyId) { favorite ->
            isFavorite = favorite
        }
    }

    return FavoriteState(
        isFavorite = isFavorite,
        toggle = { isFavorite = !isFavorite }
    )
}

@Composable
private fun rememberCurrentVacancy(state: VacancyDetailsState): androidx.compose.runtime.State<VacancyDetails?> {
    return remember(state) {
        derivedStateOf {
            when (state) {
                is VacancyDetailsState.Content -> state.vacancy
                else -> null
            }
        }
    }
}

private fun createShareIntent(vacancy: VacancyDetails): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, vacancy.url)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacancyDetailsTopBar(
    currentVacancy: VacancyDetails?,
    isFavorite: Boolean,
    onBackPressed: () -> Unit,
    onShareClick: (VacancyDetails) -> Unit,
    onFavoriteClick: (VacancyDetails, Boolean) -> Unit
) {
    val context = LocalContext.current

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
            ShareButton(
                currentVacancy = currentVacancy,
                context = context
            )
            FavoriteButton(
                currentVacancy = currentVacancy,
                isFavorite = isFavorite,
                onFavoriteClick = { vacancy, isFav ->
                    onFavoriteClick(vacancy, isFav)
                }
            )
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@Composable
private fun ShareButton(
    currentVacancy: VacancyDetails?,
    context: android.content.Context
) {
    IconButton(
        onClick = {
            currentVacancy?.let { vacancy ->
                val shareIntent = createShareIntent(vacancy)
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
}

@Composable
private fun FavoriteButton(
    currentVacancy: VacancyDetails?,
    isFavorite: Boolean,
    onFavoriteClick: (VacancyDetails, Boolean) -> Unit
) {
    IconButton(
        onClick = {
            currentVacancy?.let { vacancy ->
                onFavoriteClick(vacancy, isFavorite)
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

@Composable
private fun VacancyDetailsContentWrapper(
    modifier: Modifier,
    state: VacancyDetailsState
) {
    Box(modifier = modifier) {
        when (state) {
            is VacancyDetailsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is VacancyDetailsState.Content -> {
                VacancyDetailsContent(vacancy = state.vacancy)
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

private data class FavoriteState(
    val isFavorite: Boolean,
    val toggle: () -> Unit
)
@Composable
private fun VacancyDetailsContent(vacancy: VacancyDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PaddingMedium)
    ) {
        VacancyHeaderSection(vacancy)
        Spacer(modifier = Modifier.height(16.dp))
        VacancyEmployerSection(vacancy)
        Spacer(modifier = Modifier.height(24.dp))
        VacancyExperienceSection(vacancy)
        VacancyScheduleSection(vacancy)
        Spacer(modifier = Modifier.height(24.dp))
        VacancyDescriptionSection(vacancy)
        VacancySkillsSection(vacancy.skills)
        VacancyContactsSection(vacancy.contacts)
    }
}

@Composable
private fun VacancyHeaderSection(vacancy: VacancyDetails) {
    // Название
    Text(
        text = vacancy.name,
        fontSize = TextSize32,
        fontWeight = FontWeight.Bold,
        fontFamily = YsDisplay,
        lineHeight = LineHeight38,
        color = BlackPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Зарплата
    Text(
        text = formatSalary(vacancy.salary?.from, vacancy.salary?.to, vacancy.salary?.currency),
        fontSize = TextSize22,
        fontWeight = FontWeight.Medium,
        fontFamily = YsDisplay,
        lineHeight = LineHeight26,
        color = BlackPrimary
    )
}

@Composable
private fun VacancyEmployerSection(vacancy: VacancyDetails) {
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
}

@Composable
private fun VacancyExperienceSection(vacancy: VacancyDetails) {
    if (vacancy.experience == null) return

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

@Composable
private fun VacancyScheduleSection(vacancy: VacancyDetails) {
    Row(modifier = Modifier.fillMaxWidth()) {
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
}

@Composable
private fun VacancyDescriptionSection(vacancy: VacancyDetails) {
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
        BulletPointSection(
            title = stringResource(R.string.responsibilities_header),
            items = responsibilities
        )
    }

    // ТРЕБОВАНИЯ
    val requirements = splitIntoSentences(cleanText, "Требования")
    if (requirements.isNotEmpty()) {
        BulletPointSection(
            title = stringResource(R.string.requirements_header),
            items = requirements
        )
    }

    // УСЛОВИЯ
    val conditions = splitIntoSentences(cleanText, "Условия")
    if (conditions.isNotEmpty()) {
        BulletPointSection(
            title = stringResource(R.string.conditions_header),
            items = conditions
        )
    }
}

@Composable
private fun BulletPointSection(title: String, items: List<String>) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = YsDisplay,
        lineHeight = 19.sp,
        color = BlackPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    items.forEach { sentence ->
        Text(
            text = "$BULLET_POINT$sentence",
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

@Composable
private fun VacancySkillsSection(skills: List<String>) {
    if (skills.isEmpty()) return

    Text(
        text = stringResource(R.string.key_skills),
        fontSize = TextSize22,
        fontWeight = FontWeight.Medium,
        fontFamily = YsDisplay,
        lineHeight = LineHeight26,
        color = BlackPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    skills.forEach { skill ->
        Text(
            text = "$BULLET_POINT$skill",
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

@Composable
private fun VacancyContactsSection(contacts: ru.practicum.android.diploma.domain.models.Contacts?) {
    if (contacts == null) return

    val context = LocalContext.current

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
        EmailContact(email = contacts.email, context = context)
        Spacer(modifier = Modifier.height(8.dp))
    }

    contacts.phones.forEach { phone ->
        PhoneContact(phone = phone, context = context)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun EmailContact(email: String, context: android.content.Context) {
    Text(
        text = email,
        fontSize = TextSize16,
        fontWeight = FontWeight.Normal,
        fontFamily = YsDisplay,
        lineHeight = LineHeight19,
        color = ContactBlue,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:$email".toUri()
            }
            context.startActivity(intent)
        }
    )
}

@Composable
private fun PhoneContact(phone: ru.practicum.android.diploma.domain.models.Phone, context: android.content.Context) {
    Text(
        text = phone.formatted,
        fontSize = TextSize16,
        fontWeight = FontWeight.Normal,
        fontFamily = YsDisplay,
        lineHeight = LineHeight19,
        color = ContactBlue,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:${phone.formatted.replace(Regex("[^0-9+]"), "")}".toUri()
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
}
private fun splitIntoSentences(fullText: String, sectionName: String): List<String> {
    val sectionContent = extractSectionContent(fullText, sectionName) ?: return emptyList()
    return splitTextIntoSentences(sectionContent)
}

private fun extractSectionContent(fullText: String, sectionName: String): String? {
    val startIndex = fullText.indexOf(sectionName)
    if (startIndex == -1) return null

    val endIndex = findNextSectionIndex(fullText, sectionName, startIndex)
    val content = fullText.substring(startIndex + sectionName.length, endIndex).trim()

    return content.ifEmpty { null }
}

private fun findNextSectionIndex(fullText: String, currentSection: String, startIndex: Int): Int {
    val nextSections = listOf("Обязанности", "Требования", "Условия")
    var endIndex = fullText.length

    for (nextSection in nextSections) {
        if (nextSection != currentSection) {
            val nextIndex = fullText.indexOf(nextSection, startIndex + currentSection.length)
            if (nextIndex != -1 && nextIndex < endIndex) {
                endIndex = nextIndex
            }
        }
    }

    return endIndex
}

private fun splitTextIntoSentences(content: String): List<String> {
    val result = mutableListOf<String>()
    var currentSentence = StringBuilder()

    for (char in content) {
        currentSentence.append(char)
        if (isSentenceEnding(char)) {
            val sentence = processSentence(currentSentence.toString())
            if (sentence.isNotEmpty()) {
                result.add(sentence)
            }
            currentSentence = StringBuilder()
        }
    }

    return result
}

private fun isSentenceEnding(char: Char): Boolean {
    return char == '.' || char == '!' || char == '?'
}

private fun processSentence(rawSentence: String): String {
    var sentence = rawSentence.trim()
    if (sentence.isEmpty()) return ""

    sentence = removeBulletPrefix(sentence)
    sentence = normalizeWhitespace(sentence)

    return if (sentence.isNotEmpty()) {
        capitalizeFirstLetter(sentence)
    } else {
        ""
    }
}

private fun removeBulletPrefix(text: String): String {
    return text.replace(Regex("^[•\\-*\\d+.]\\s*"), "")
}

private fun normalizeWhitespace(text: String): String {
    return text.replace(Regex("\\s+"), " ")
}

private fun capitalizeFirstLetter(text: String): String {
    val firstChar = text[0].uppercaseChar()
    val rest = if (text.length > 1) text.substring(1) else ""
    return firstChar + rest
}

@Composable
private fun ErrorPlaceholder(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error_cat),
            contentDescription = null,
            modifier = Modifier.size(328.dp, 223.dp),
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
