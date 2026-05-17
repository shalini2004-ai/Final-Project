package com.raitha.bharosa.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raitha.bharosa.data.models.*
import com.raitha.bharosa.ui.theme.*

@Composable
fun MarketScreen(
    marketRates: List<MarketRate>,
    selectedLanguage: AppLanguage
) {
    var sortByPrice by remember { mutableStateOf(false) }
    var filterTrend by remember { mutableStateOf<PriceTrend?>(null) }

    val displayRates = remember(marketRates, sortByPrice, filterTrend) {
        var rates = if (filterTrend != null) marketRates.filter { it.trend == filterTrend } else marketRates
        if (sortByPrice) rates = rates.sortedByDescending { it.pricePerQuintal }
        rates
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(EarthBrown)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    getLangText(selectedLanguage, "APMC Market Rates", "APMC ಮಾರುಕಟ್ಟೆ ದರ", "APMC बाजार भाव", "APMC మార్కెట్ రేట్లు"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    getLangText(selectedLanguage, "Karnataka Mandi Prices — Updated Today", "ಕರ್ನಾಟಕ ಮಂಡಿ ದರ — ಇಂದು ನವೀಕರಿಸಲಾಗಿದೆ", "कर्नाटक मंडी भाव — आज अपडेट", "కర్ణాటక మండి ధరలు — నేడు నవీకరించబడ్డాయి"),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        // Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = sortByPrice,
                onClick = { sortByPrice = !sortByPrice },
                label = { Text(getLangText(selectedLanguage, "Sort by Price", "ಬೆಲೆಯಿಂದ ವಿಂಗಡಿಸಿ", "कीमत से क्रमबद्ध", "ధర ప్రకారం వరుసపెట్టు")) },
                leadingIcon = { Icon(Icons.Default.Sort, null, Modifier.size(16.dp)) }
            )

            PriceTrend.values().forEach { trend ->
                val trendColor = when (trend) {
                    PriceTrend.RISING -> Color(0xFF4CAF50)
                    PriceTrend.FALLING -> Color(0xFFF44336)
                    PriceTrend.STABLE -> Color(0xFFFF9800)
                }
                FilterChip(
                    selected = filterTrend == trend,
                    onClick = { filterTrend = if (filterTrend == trend) null else trend },
                    label = { Text(trend.label, color = if (filterTrend == trend) Color.White else trendColor) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = trendColor
                    )
                )
            }
        }

        // Summary Cards
        MarketSummaryRow(marketRates)

        // Rates List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(displayRates) { rate ->
                MarketRateCard(rate, selectedLanguage)
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun MarketSummaryRow(rates: List<MarketRate>) {
    val rising = rates.count { it.trend == PriceTrend.RISING }
    val falling = rates.count { it.trend == PriceTrend.FALLING }
    val stable = rates.count { it.trend == PriceTrend.STABLE }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryCard("↑ Rising", rising.toString(), Color(0xFF4CAF50), Modifier.weight(1f))
        SummaryCard("→ Stable", stable.toString(), Color(0xFFFF9800), Modifier.weight(1f))
        SummaryCard("↓ Falling", falling.toString(), Color(0xFFF44336), Modifier.weight(1f))
    }
}

@Composable
fun SummaryCard(label: String, count: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(count, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = color)
            Text(label, fontSize = 11.sp, color = color.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun MarketRateCard(rate: MarketRate, language: AppLanguage) {
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(rate.cropName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${rate.market} • ${rate.state}", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Min: ₹${rate.minPrice.toInt()} — Max: ₹${rate.maxPrice.toInt()}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "₹${rate.pricePerQuintal.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = FarmGreenDark
                )
                Text("per Quintal", color = Color.Gray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = trendColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        "$trendIcon ${rate.trend.label}",
                        color = trendColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
