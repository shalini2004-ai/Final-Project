package com.raitha.bharosa.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raitha.bharosa.data.models.*
import com.raitha.bharosa.ui.theme.*

@Composable
fun CropsScreen(
    crops: List<Crop>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedLanguage: AppLanguage
) {
    var selectedCrop by remember { mutableStateOf<Crop?>(null) }
    var viewMode by remember { mutableStateOf(false) } // false = grid, true = list

    if (selectedCrop != null) {
        CropDetailScreen(
            crop = selectedCrop!!,
            language = selectedLanguage,
            onBack = { selectedCrop = null }
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GoldenYellow)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    getLangText(selectedLanguage, "Crop Encyclopedia", "ಬೆಳೆ ವಿಶ್ವಕೋಶ", "फसल विश्वकोश", "పంట విజ్ఞాన సర్వస్వం"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = SoilDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${crops.size} ${getLangText(selectedLanguage, "crops available", "ಬೆಳೆಗಳು ಲಭ್ಯ", "फसलें उपलब्ध", "పంటలు అందుబాటులో ఉన్నాయి")}",
                    color = SoilDark.copy(alpha = 0.75f),
                    fontSize = 13.sp
                )
            }
        }

        // Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text(getLangText(selectedLanguage, "Search crops...", "ಬೆಳೆ ಹುಡುಕಿ...", "फसल खोजें...", "పంట వెతకండి..."))
            },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Clear, null)
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldenYellow,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f)
            )
        )

        if (crops.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔍", fontSize = 48.sp)
                    Text(
                        getLangText(selectedLanguage, "No crops found", "ಯಾವ ಬೆಳೆ ಕಂಡಿಲ್ಲ", "कोई फसल नहीं मिली", "పంట కనుగొనబడలేదు"),
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(crops) { crop ->
                    CropCard(crop, selectedLanguage) { selectedCrop = crop }
                }
                item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun CropCard(crop: Crop, language: AppLanguage, onClick: () -> Unit) {
    val cropName = when (language) {
        AppLanguage.KANNADA -> crop.nameKn
        AppLanguage.HINDI -> crop.nameHi
        AppLanguage.TELUGU -> crop.nameTe
        else -> crop.nameEn
    }

    val categoryColor = when (crop.category) {
        CropCategory.CEREAL -> Color(0xFF8D6E63)
        CropCategory.VEGETABLE -> FarmGreen
        CropCategory.FRUIT -> Color(0xFFE91E63)
        CropCategory.PULSE -> Color(0xFF7986CB)
        CropCategory.OILSEED -> Color(0xFFFF8F00)
        CropCategory.SPICE -> Color(0xFFD32F2F)
        CropCategory.CASH_CROP -> GoldenYellow
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = categoryColor.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Emoji
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(crop.category.emoji, fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                cropName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (language != AppLanguage.ENGLISH) {
                Text(crop.nameEn, color = Color.Gray, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = categoryColor.copy(alpha = 0.15f)
            ) {
                Text(
                    crop.category.displayName,
                    color = categoryColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⏱️ ${crop.harvestDays}d", fontSize = 11.sp, color = Color.Gray)
                Text("💧 ${crop.waterRequirement.label}", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun CropDetailScreen(crop: Crop, language: AppLanguage, onBack: () -> Unit) {
    val cropName = when (language) {
        AppLanguage.KANNADA -> crop.nameKn
        AppLanguage.HINDI -> crop.nameHi
        AppLanguage.TELUGU -> crop.nameTe
        else -> crop.nameEn
    }

    val monthNames = listOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FarmGreen)
                .padding(top = 48.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Text(crop.category.emoji, fontSize = 56.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(cropName, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text(crop.nameEn, color = Color.White.copy(alpha = 0.8f))
                Text(crop.category.displayName, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Names in all languages
            DetailCard("🌐 Names") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    LangNameChip("ಕನ್ನಡ", crop.nameKn)
                    LangNameChip("हिन्दी", crop.nameHi)
                    LangNameChip("తెలుగు", crop.nameTe)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Key Info
            DetailCard("📋 " + getLangText(language, "Key Information", "ಮುಖ್ಯ ಮಾಹಿತಿ", "मुख्य जानकारी", "ముఖ్యమైన సమాచారం")) {
                InfoRow("🌾 Category", crop.category.displayName)
                InfoRow("⏱️ Harvest Duration", "${crop.harvestDays} days")
                InfoRow("💧 Water Need", crop.waterRequirement.label)
                InfoRow("🪴 Soil Types", crop.soilTypes.joinToString(", "))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sowing Months
            DetailCard("📅 " + getLangText(language, "Ideal Sowing Months", "ಸೂಕ್ತ ಬಿತ್ತನೆ ತಿಂಗಳುಗಳು", "उचित बुवाई महीने", "అనుకూలమైన విత్తన నెలలు")) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    crop.sowingMonths.forEach { month ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FarmGreen
                        ) {
                            Text(
                                monthNames[month],
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            if (crop.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                DetailCard("ℹ️ " + getLangText(language, "About", "ಬಗ್ಗೆ", "के बारे में", "గురించి")) {
                    Text(crop.description, fontSize = 14.sp, lineHeight = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun DetailCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = FarmGreenDark)
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
    Divider(color = Color.Gray.copy(alpha = 0.1f))
}

@Composable
fun LangNameChip(lang: String, name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(lang, fontSize = 10.sp, color = Color.Gray)
        Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}
