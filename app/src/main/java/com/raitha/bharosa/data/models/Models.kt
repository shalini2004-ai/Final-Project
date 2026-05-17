package com.raitha.bharosa.data.models

data class Crop(
    val id: String,
    val nameEn: String,
    val nameKn: String, // Kannada
    val nameHi: String, // Hindi
    val nameTe: String, // Telugu
    val category: CropCategory,
    val sowingMonths: List<Int>, // 1-12
    val harvestDays: Int,
    val waterRequirement: WaterRequirement,
    val soilTypes: List<String>,
    val imageRes: String = "",
    val description: String = ""
)

enum class CropCategory(val displayName: String, val emoji: String) {
    CEREAL("Cereals", "🌾"),
    VEGETABLE("Vegetables", "🥦"),
    FRUIT("Fruits", "🍎"),
    PULSE("Pulses", "🫘"),
    OILSEED("Oilseeds", "🌻"),
    SPICE("Spices", "🌶️"),
    CASH_CROP("Cash Crops", "💰")
}

enum class WaterRequirement(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    VERY_HIGH("Very High")
}

data class MarketRate(
    val cropName: String,
    val market: String,
    val state: String,
    val pricePerQuintal: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val date: String,
    val trend: PriceTrend = PriceTrend.STABLE
)

enum class PriceTrend(val label: String, val color: Long) {
    RISING("Rising", 0xFF4CAF50),
    FALLING("Falling", 0xFFF44336),
    STABLE("Stable", 0xFFFF9800)
}

data class WeatherInfo(
    val location: String,
    val temperature: Double,
    val humidity: Int,
    val rainfall: Double,
    val condition: String,
    val windSpeed: Double,
    val forecast: List<DayForecast> = emptyList()
)

data class DayForecast(
    val day: String,
    val condition: String,
    val maxTemp: Double,
    val minTemp: Double,
    val rainfall: Double
)

data class SowingWindow(
    val crop: Crop,
    val isIdealNow: Boolean,
    val daysUntilIdeal: Int,
    val recommendation: String,
    val soilMoisture: String = "Optimal"
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val language: AppLanguage = AppLanguage.ENGLISH
)

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    HINDI("hi", "Hindi", "हिन्दी"),
    TELUGU("te", "Telugu", "తెలుగు")
}

data class FarmerProfile(
    val name: String = "",
    val location: String = "",
    val landAreaAcres: Double = 0.0,
    val preferredLanguage: AppLanguage = AppLanguage.KANNADA,
    val crops: List<String> = emptyList()
)

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Idle : UiState<Nothing>()
}
