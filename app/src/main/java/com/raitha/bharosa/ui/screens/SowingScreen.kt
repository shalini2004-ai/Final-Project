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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raitha.bharosa.data.models.*
import com.raitha.bharosa.ui.theme.*

@Composable
fun SowingScreen(
    sowingWindows: List<SowingWindow>,
    selectedLanguage: AppLanguage
) {
    var selectedCategory by remember { mutableStateOf<CropCategory?>(null) }

    val filtered = remember(sowingWindows, selectedCategory) {
        if (selectedCategory == null) sowingWindows
        else sowingWindows.filter { it.crop.category == selectedCategory }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FarmGreen)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    getLangText(selectedLanguage, "Sowing Calendar", "ಬಿತ್ತನೆ ಕ್ಯಾಲೆಂಡರ್", "बुवाई कैलेंडर", "విత్తన క్యాలెండర్"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    getLangText(selectedLanguage, "Best crops to sow this month", "ಈ ತಿಂಗಳು ಬಿತ್ತಲು ಉತ್ತಮ ಬೆಳೆಗಳು", "इस महीने बोने के लिए सर्वश्रेष्ठ फसलें", "ఈ నెల విత్తడానికి ఉత్తమ పంటలు"),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        // Category Filter
        LazyRow(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text(getLangText(selectedLanguage, "All", "ಎಲ್ಲಾ", "सभी", "అన్నీ")) }
                )
            }
            items(CropCategory.values()) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = if (selectedCategory == category) null else category },
                    label = { Text("${category.emoji} ${category.displayName}") }
                )
            }
        }

        // Ideal Now section
        val idealNow = filtered.filter { it.isIdealNow }
        val upcoming = filtered.filter { !it.isIdealNow }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (idealNow.isNotEmpty()) {
                item {
                    Text(
                        "✅ " + getLangText(selectedLanguage, "Ideal to Sow Now", "ಈಗ ಬಿತ್ತಲು ಸೂಕ್ತ", "अभी बोने के लिए उचित", "ఇప్పుడు విత్తడానికి అనుకూలం"),
                        fontWeight = FontWeight.Bold,
                        color = FarmGreen,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(idealNow) { window ->
                    SowingWindowCard(window, selectedLanguage, isIdeal = true)
                }
            }

            if (upcoming.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⏳ " + getLangText(selectedLanguage, "Upcoming Sowing", "ಮುಂದಿನ ಬಿತ್ತನೆ", "आगामी बुवाई", "రాబోయే విత్తనం"),
                        fontWeight = FontWeight.Bold,
                        color = EarthBrown,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(upcoming) { window ->
                    SowingWindowCard(window, selectedLanguage, isIdeal = false)
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SowingWindowCard(window: SowingWindow, language: AppLanguage, isIdeal: Boolean) {
    val cropName = when (language) {
        AppLanguage.KANNADA -> window.crop.nameKn
        AppLanguage.HINDI -> window.crop.nameHi
        AppLanguage.TELUGU -> window.crop.nameTe
        else -> window.crop.nameEn
    }

    val borderColor = if (isIdeal) FarmGreen else Color.Gray.copy(alpha = 0.4f)
    val bgColor = if (isIdeal) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.5.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Crop emoji in circle
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (isIdeal) FarmGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(window.crop.category.emoji, fontSize = 26.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(cropName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    window.crop.nameEn + " • " + window.crop.category.displayName,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    window.recommendation,
                    color = if (isIdeal) FarmGreen else EarthBrown,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniInfoChip("⏱️ ${window.crop.harvestDays}d", Color(0xFF7986CB))
                    MiniInfoChip("💧 ${window.crop.waterRequirement.label}", Color(0xFF4FC3F7))
                }
            }
        }
    }
}

@Composable
fun MiniInfoChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
