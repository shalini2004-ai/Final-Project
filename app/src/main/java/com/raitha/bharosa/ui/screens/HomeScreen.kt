package com.raitha.bharosa.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raitha.bharosa.data.models.*
import com.raitha.bharosa.ui.theme.*
import java.util.Calendar

@Composable
fun HomeScreen(
    weather: WeatherInfo?,
    sowingWindows: List<SowingWindow>,
    marketRates: List<MarketRate>,
    selectedLanguage: AppLanguage,
    onNavigateToSowing: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToCrops: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Hero Header
        HeroHeader(weather, selectedLanguage)

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Action Cards
        QuickActionGrid(
            onNavigateToSowing = onNavigateToSowing,
            onNavigateToMarket = onNavigateToMarket,
            onNavigateToChat = onNavigateToChat,
            onNavigateToCrops = onNavigateToCrops,
            language = selectedLanguage
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sowing Alert Banner
        val idealCrops = sowingWindows.filter { it.isIdealNow }.take(3)
        if (idealCrops.isNotEmpty()) {
            SowingAlertBanner(idealCrops, selectedLanguage)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Market Highlights
        MarketHighlights(marketRates.take(5), selectedLanguage, onNavigateToMarket)

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Forecast
        weather?.let {
            WeatherForecastCard(it)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HeroHeader(weather: WeatherInfo?, language: AppLanguage) {
    val greeting = when {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12 -> when (language) {
            AppLanguage.KANNADA -> "ಶುಭೋದಯ"
            AppLanguage.HINDI -> "सुप्रभात"
            AppLanguage.TELUGU -> "శుభోదయం"
            else -> "Good Morning"
        }
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 17 -> when (language) {
            AppLanguage.KANNADA -> "ಶುಭ ಮಧ್ಯಾಹ್ನ"
            AppLanguage.HINDI -> "नमस्ते"
            AppLanguage.TELUGU -> "నమస్కారం"
            else -> "Good Afternoon"
        }
        else -> when (language) {
            AppLanguage.KANNADA -> "ಶುಭ ಸಂಜೆ"
            AppLanguage.HINDI -> "शुभ संध्या"
            AppLanguage.TELUGU -> "శుభ సాయంత్రం"
            else -> "Good Evening"
        }
    }

    val appName = when (language) {
        AppLanguage.KANNADA -> "ರೈತ ಭರೋಸ ಹಬ್"
        AppLanguage.HINDI -> "रैथा भरोसा हब"
        AppLanguage.TELUGU -> "రైతు భరోసా హబ్"
        else -> "Raitha Bharosa Hub"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FarmGreenDark, FarmGreen, FarmGreenLight)
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$greeting! 🌾",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = appName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Weather mini card
                weather?.let {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("☁️", fontSize = 24.sp)
                            Text(
                                "${it.temperature}°C",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                it.location.split(",").first(),
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weather?.let {
                    StatChip("💧 ${it.humidity}%", "Humidity")
                    StatChip("🌬️ ${it.windSpeed} km/h", "Wind")
                    StatChip("🌧️ ${it.rainfall} mm", "Rainfall")
                }
            }
        }
    }
}

@Composable
fun StatChip(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
    }
}

@Composable
fun QuickActionGrid(
    onNavigateToSowing: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToCrops: () -> Unit,
    language: AppLanguage
) {
    val actions = listOf(
        QuickAction("🌱", getLangText(language, "Sowing Guide", "ಬಿತ್ತನೆ ಮಾರ್ಗದರ್ಶಿ", "बुवाई गाइड", "విత్తన మార్గదర్శి"), FarmGreen, onNavigateToSowing),
        QuickAction("📊", getLangText(language, "Market Rates", "ಮಾರುಕಟ್ಟೆ ದರ", "बाजार भाव", "మార్కెట్ రేట్లు"), EarthBrown, onNavigateToMarket),
        QuickAction("🤖", getLangText(language, "AI Expert", "AI ತಜ್ಞ", "AI विशेषज्ञ", "AI నిపుణుడు"), SkyBlue, onNavigateToChat),
        QuickAction("🌾", getLangText(language, "Crop Info", "ಬೆಳೆ ಮಾಹಿತಿ", "फसल जानकारी", "పంట సమాచారం"), GoldenYellow, onNavigateToCrops)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        actions.forEach { action ->
            QuickActionCard(
                action = action,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

data class QuickAction(val emoji: String, val label: String, val color: Color, val onClick: () -> Unit)

@Composable
fun QuickActionCard(action: QuickAction, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { action.onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = action.color.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, action.color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(action.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelLarge,
                color = action.color,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SowingAlertBanner(idealCrops: List<SowingWindow>, language: AppLanguage) {
    val title = getLangText(language, "🌱 Ideal Sowing Now!", "🌱 ಈಗ ಬಿತ್ತನೆ ಸೂಕ್ತ!", "🌱 अभी बुवाई उचित!", "🌱 ఇప్పుడు విత్తనానికి అనుకూలం!")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        border = BorderStroke(1.5.dp, FarmGreen)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = FarmGreenDark, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                idealCrops.forEach { window ->
                    val cropName = when (language) {
                        AppLanguage.KANNADA -> window.crop.nameKn
                        AppLanguage.HINDI -> window.crop.nameHi
                        AppLanguage.TELUGU -> window.crop.nameTe
                        else -> window.crop.nameEn
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = FarmGreen
                    ) {
                        Text(
                            "${window.crop.category.emoji} $cropName",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MarketHighlights(rates: List<MarketRate>, language: AppLanguage, onSeeAll: () -> Unit) {
    val title = getLangText(language, "Market Rates Today", "ಇಂದಿನ ಮಾರುಕಟ್ಟೆ ದರ", "आज के बाजार भाव", "నేటి మార్కెట్ రేట్లు")

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            TextButton(onClick = onSeeAll) {
                Text(getLangText(language, "See All", "ಎಲ್ಲಾ ನೋಡಿ", "सब देखें", "అన్నీ చూడండి"))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        rates.forEach { rate ->
            MarketRateRow(rate)
            Divider(color = Color.Gray.copy(alpha = 0.15f))
        }
    }
}

@Composable
fun MarketRateRow(rate: MarketRate) {
    val trendColor = when (rate.trend) {
        PriceTrend.RISING -> Color(0xFF4CAF50)
        PriceTrend.FALLING -> Color(0xFFF44336)
        PriceTrend.STABLE -> Color(0xFFFF9800)
    }
    val trendIcon = when (rate.trend) {
        PriceTrend.RISING -> "↑"
        PriceTrend.FALLING -> "↓"
        PriceTrend.STABLE -> "→"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(rate.cropName, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(rate.market, color = Color.Gray, fontSize = 11.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "₹${rate.pricePerQuintal.toInt()}/Q",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = trendColor.copy(alpha = 0.15f)
            ) {
                Text(
                    "$trendIcon ${rate.trend.label}",
                    color = trendColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
fun WeatherForecastCard(weather: WeatherInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "5-Day Forecast 🌤️",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = SkyBlue
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weather.forecast.forEach { day ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(day.day, fontSize = 11.sp, color = Color.Gray)
                        Text(
                            when {
                                day.condition.contains("Rain", true) -> "🌧️"
                                day.condition.contains("Cloud", true) -> "⛅"
                                else -> "☀️"
                            },
                            fontSize = 22.sp
                        )
                        Text("${day.maxTemp.toInt()}°", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${day.minTemp.toInt()}°", color = Color.Gray, fontSize = 11.sp)
                        if (day.rainfall > 0) {
                            Text("${day.rainfall}mm", color = SkyBlue, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

fun getLangText(language: AppLanguage, en: String, kn: String, hi: String, te: String): String {
    return when (language) {
        AppLanguage.KANNADA -> kn
        AppLanguage.HINDI -> hi
        AppLanguage.TELUGU -> te
        else -> en
    }
}
