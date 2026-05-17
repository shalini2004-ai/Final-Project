package com.raitha.bharosa.data.repository

import com.raitha.bharosa.data.models.*
import java.util.Calendar

object CropRepository {

    val allCrops = listOf(
        Crop(
            id = "rice",
            nameEn = "Rice",
            nameKn = "ಭತ್ತ",
            nameHi = "धान",
            nameTe = "వరి",
            category = CropCategory.CEREAL,
            sowingMonths = listOf(6, 7, 8),
            harvestDays = 120,
            waterRequirement = WaterRequirement.VERY_HIGH,
            soilTypes = listOf("Clay", "Loamy"),
            description = "Staple food crop requiring flooded fields"
        ),
        Crop(
            id = "wheat",
            nameEn = "Wheat",
            nameKn = "ಗೋಧಿ",
            nameHi = "गेहूं",
            nameTe = "గోధుమ",
            category = CropCategory.CEREAL,
            sowingMonths = listOf(11, 12),
            harvestDays = 110,
            waterRequirement = WaterRequirement.MEDIUM,
            soilTypes = listOf("Loamy", "Clay Loam"),
            description = "Rabi crop grown in winter months"
        ),
        Crop(
            id = "maize",
            nameEn = "Maize",
            nameKn = "ಜೋಳ",
            nameHi = "मक्का",
            nameTe = "మొక్కజొన్న",
            category = CropCategory.CEREAL,
            sowingMonths = listOf(6, 7, 10, 11),
            harvestDays = 90,
            waterRequirement = WaterRequirement.MEDIUM,
            soilTypes = listOf("Loamy", "Sandy Loam"),
            description = "Versatile crop for food and fodder"
        ),
        Crop(
            id = "tomato",
            nameEn = "Tomato",
            nameKn = "ಟೊಮ್ಯಾಟೊ",
            nameHi = "टमाटर",
            nameTe = "టమాటా",
            category = CropCategory.VEGETABLE,
            sowingMonths = listOf(6, 7, 10, 11, 12),
            harvestDays = 75,
            waterRequirement = WaterRequirement.MEDIUM,
            soilTypes = listOf("Sandy Loam", "Loamy"),
            description = "High-value vegetable crop"
        ),
        Crop(
            id = "cotton",
            nameEn = "Cotton",
            nameKn = "ಹತ್ತಿ",
            nameHi = "कपास",
            nameTe = "పత్తి",
            category = CropCategory.CASH_CROP,
            sowingMonths = listOf(5, 6, 7),
            harvestDays = 180,
            waterRequirement = WaterRequirement.MEDIUM,
            soilTypes = listOf("Black Cotton Soil", "Loamy"),
            description = "Major cash crop of Karnataka"
        ),
        Crop(
            id = "sugarcane",
            nameEn = "Sugarcane",
            nameKn = "ಕಬ್ಬು",
            nameHi = "गन्ना",
            nameTe = "చెరకు",
            category = CropCategory.CASH_CROP,
            sowingMonths = listOf(1, 2, 10, 11),
            harvestDays = 365,
            waterRequirement = WaterRequirement.HIGH,
            soilTypes = listOf("Loamy", "Clay Loam"),
            description = "Long duration cash crop"
        ),
        Crop(
            id = "ragi",
            nameEn = "Ragi (Finger Millet)",
            nameKn = "ರಾಗಿ",
            nameHi = "रागी",
            nameTe = "రాగి",
            category = CropCategory.CEREAL,
            sowingMonths = listOf(6, 7),
            harvestDays = 100,
            waterRequirement = WaterRequirement.LOW,
            soilTypes = listOf("Red Soil", "Sandy Loam", "Loamy"),
            description = "Drought-resistant millet, staple of Karnataka"
        ),
        Crop(
            id = "groundnut",
            nameEn = "Groundnut",
            nameKn = "ಶೇಂಗಾ",
            nameHi = "मूंगफली",
            nameTe = "వేరుశెనగ",
            category = CropCategory.OILSEED,
            sowingMonths = listOf(6, 7, 10),
            harvestDays = 120,
            waterRequirement = WaterRequirement.MEDIUM,
            soilTypes = listOf("Sandy Loam", "Red Soil"),
            description = "Major oilseed crop"
        ),
        Crop(
            id = "onion",
            nameEn = "Onion",
            nameKn = "ಈರುಳ್ಳಿ",
            nameHi = "प्याज",
            nameTe = "ఉల్లిపాయ",
            category = CropCategory.VEGETABLE,
            sowingMonths = listOf(10, 11, 12, 1),
            harvestDays = 120,
            waterRequirement = WaterRequirement.MEDIUM,
            soilTypes = listOf("Sandy Loam", "Loamy"),
            description = "Important vegetable and export crop"
        ),
        Crop(
            id = "arhar",
            nameEn = "Pigeon Pea (Tur Dal)",
            nameKn = "ತೊಗರಿ",
            nameHi = "अरहर",
            nameTe = "కంది",
            category = CropCategory.PULSE,
            sowingMonths = listOf(6, 7),
            harvestDays = 150,
            waterRequirement = WaterRequirement.LOW,
            soilTypes = listOf("Red Soil", "Loamy", "Sandy Loam"),
            description = "Important pulse crop, drought tolerant"
        )
    )

    fun getSowingRecommendations(currentMonth: Int): List<SowingWindow> {
        return allCrops.map { crop ->
            val isIdeal = currentMonth in crop.sowingMonths
            val daysUntil = if (isIdeal) 0 else {
                val nextMonth = crop.sowingMonths.firstOrNull { it > currentMonth }
                    ?: crop.sowingMonths.first()
                val monthDiff = if (nextMonth > currentMonth) nextMonth - currentMonth
                else 12 - currentMonth + nextMonth
                monthDiff * 30
            }
            SowingWindow(
                crop = crop,
                isIdealNow = isIdeal,
                daysUntilIdeal = daysUntil,
                recommendation = if (isIdeal) "✅ Ideal time to sow now!"
                else "⏳ Ideal in ~$daysUntil days"
            )
        }.sortedWith(compareByDescending<SowingWindow> { it.isIdealNow }.thenBy { it.daysUntilIdeal })
    }

    fun getMarketRates(): List<MarketRate> {
        return listOf(
            MarketRate("Ragi", "APMC Bangalore", "Karnataka", 3800.0, 3600.0, 4000.0, "Today", PriceTrend.RISING),
            MarketRate("Rice", "APMC Mysore", "Karnataka", 2200.0, 2000.0, 2400.0, "Today", PriceTrend.STABLE),
            MarketRate("Tomato", "APMC Bangalore", "Karnataka", 1200.0, 800.0, 1500.0, "Today", PriceTrend.FALLING),
            MarketRate("Onion", "APMC Hubli", "Karnataka", 1800.0, 1600.0, 2100.0, "Today", PriceTrend.RISING),
            MarketRate("Cotton", "APMC Raichur", "Karnataka", 6800.0, 6500.0, 7200.0, "Today", PriceTrend.STABLE),
            MarketRate("Maize", "APMC Davangere", "Karnataka", 1900.0, 1750.0, 2100.0, "Today", PriceTrend.RISING),
            MarketRate("Groundnut", "APMC Bellary", "Karnataka", 5200.0, 4800.0, 5600.0, "Today", PriceTrend.STABLE),
            MarketRate("Wheat", "APMC Bidar", "Karnataka", 2400.0, 2200.0, 2600.0, "Today", PriceTrend.RISING),
            MarketRate("Pigeon Pea", "APMC Gulbarga", "Karnataka", 7200.0, 6800.0, 7600.0, "Today", PriceTrend.FALLING),
            MarketRate("Sugarcane", "APMC Mandya", "Karnataka", 3200.0, 3000.0, 3500.0, "Today", PriceTrend.STABLE)
        )
    }

    fun getMockWeather(): WeatherInfo {
        return WeatherInfo(
            location = "Bangalore, Karnataka",
            temperature = 27.5,
            humidity = 65,
            rainfall = 0.0,
            condition = "Partly Cloudy",
            windSpeed = 12.0,
            forecast = listOf(
                DayForecast("Today", "Partly Cloudy", 30.0, 22.0, 0.0),
                DayForecast("Tomorrow", "Light Rain", 27.0, 20.0, 5.0),
                DayForecast("Wed", "Heavy Rain", 24.0, 19.0, 25.0),
                DayForecast("Thu", "Cloudy", 26.0, 21.0, 2.0),
                DayForecast("Fri", "Sunny", 31.0, 23.0, 0.0)
            )
        )
    }
}
