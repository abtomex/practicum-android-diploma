package ru.practicum.android.diploma.presentation.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==================== ОТСТУПЫ ====================
val PaddingSmall = 8.dp
val PaddingMedium = 16.dp

// Базовые отступы между элементами
val SpaceSmall = 4.dp
val SpaceMedium = 8.dp
val SpaceLarge = 16.dp
val SpaceXLarge = 24.dp

// ==================== РАЗМЕРЫ ИКОНОК И ЛОГОТИПОВ ====================
val IconSizeDefault = 24.dp
val LogoSizeMedium = 48.dp
val ErrorIconSize = 120.dp

// ==================== РАДИУСЫ СКРУГЛЕНИЙ ====================
val CornerRadiusSmall = 12.dp
val CornerRadiusMedium = 16.dp

// ==================== РАЗМЕРЫ ТЕКСТОВ ====================
// Базовые размеры
val TextSize32 = 32.sp       // Название вакансии
val TextSize22 = 22.sp       // Зарплата, компания, разделы, заголовки
val TextSize18 = 18.sp       // Ошибки
val TextSize16 = 16.sp       // Основной текст (город, опыт, навыки, контакты)
val TextSize14 = 14.sp       // Вспомогательный текст (чипсы, комментарии)

// Алиасы для удобства использования
val TitleSize22 = TextSize22
val TitleSize18 = TextSize18

// Алиасы для экрана "Вакансия" (VacancyDetailsScreen)
val VacancyTitleFontSize = TextSize32
val SalaryFontSize = TextSize22
val CompanyNameFontSize = TextSize22
val SectionTitleFontSize = TextSize22
val SubsectionTitleFontSize = TextSize16
val TextRegularFontSize = TextSize16
val ErrorTextFontSize = TextSize18
val TopBarTitleFontSize = TextSize22
val ChipTextFontSize = TextSize14

// Для экрана "Избранное" (VacancyCardItem)
val FavoriteTitleFontSize = TextSize22
val FavoriteCompanyFontSize = TextSize16
val FavoriteSalaryFontSize = TextSize16

// ==================== ВЫСОТА СТРОКИ (lineHeight) ====================
// Базовые значения
val LineHeight38 = 38.sp      // для текста 32px
val LineHeight26 = 26.sp      // для текста 22px
val LineHeight19 = 19.sp      // для текста 16px и 14px

// Алиасы для экрана "Вакансия"
val VacancyTitleLineHeight = LineHeight38
val SalaryLineHeight = LineHeight26
val CompanyNameLineHeight = LineHeight26
val SectionTitleLineHeight = LineHeight26
val SubsectionTitleLineHeight = LineHeight19
val TextRegularLineHeight = LineHeight19
val ErrorTextLineHeight = LineHeight26
val TopBarTitleLineHeight = LineHeight26

// Алиасы для экрана "Избранное"
val FavoriteTitleLineHeight = LineHeight26
val FavoriteCompanyLineHeight = LineHeight19
val FavoriteSalaryLineHeight = LineHeight19

// ==================== КОНСТАНТЫ ДЛЯ ОТСТУПОВ В КОНКРЕТНЫХ МЕСТАХ ====================
val SpaceBetweenListItems = SpaceSmall
val SpaceAfterSubsectionTitle = SpaceMedium
val SpaceBetweenSections = SpaceLarge
val SpaceAfterSkills = SpaceXLarge
val SpaceBetweenContactItems = SpaceSmall
val LogoTextSpacing = PaddingSmall
val ExperienceSpacing = SpaceSmall
val ErrorIconSpacing = SpaceLarge
