package com.raitha.bharosa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.raitha.bharosa.BuildConfig
import com.raitha.bharosa.data.models.*
import com.raitha.bharosa.data.repository.CropRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel : ViewModel() {

    // ✅ SAFE Gemini initialization
    private val generativeModel: GenerativeModel? by lazy {
        try {
            GenerativeModel(
                modelName = "gemini-pro",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Language
    private val _selectedLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    // Sowing
    private val _sowingWindows = MutableStateFlow<List<SowingWindow>>(emptyList())
    val sowingWindows = _sowingWindows.asStateFlow()

    // Market
    private val _marketRates = MutableStateFlow<List<MarketRate>>(emptyList())
    val marketRates = _marketRates.asStateFlow()

    // Weather
    private val _weather = MutableStateFlow<WeatherInfo?>(null)
    val weather = _weather.asStateFlow()

    // Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    private val _chatLoading = MutableStateFlow(false)
    val chatLoading = _chatLoading.asStateFlow()

    // Profile
    private val _farmerProfile = MutableStateFlow(FarmerProfile())
    val farmerProfile = _farmerProfile.asStateFlow()

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filteredCrops = MutableStateFlow<List<Crop>>(emptyList())
    val filteredCrops = _filteredCrops.asStateFlow()

    init {
        loadData()
        addWelcomeMessage()
    }

    // ---------------- LOAD DATA ----------------
    private fun loadData() {
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        _sowingWindows.value = CropRepository.getSowingRecommendations(month)
        _marketRates.value = CropRepository.getMarketRates()
        _weather.value = CropRepository.getMockWeather()
        _filteredCrops.value = CropRepository.allCrops
    }

    // ---------------- WELCOME ----------------
    private fun addWelcomeMessage() {
        _chatMessages.value = listOf(
            ChatMessage(
                content = getWelcomeMessage(_selectedLanguage.value),
                isUser = false
            )
        )
    }

    private fun getWelcomeMessage(language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> "ನಮಸ್ಕಾರ! ನಾನು ನಿಮ್ಮ ಕೃಷಿ ಸಹಾಯಕ 🌾"
            AppLanguage.HINDI -> "नमस्ते! मैं आपका कृषि सहायक हूँ 🌾"
            AppLanguage.TELUGU -> "నమస్కారం! నేను మీ వ్యవసాయ సహాయకుడిని 🌾"
            else -> "Welcome! I'm your Smart Farming Assistant 🌾"
        }
    }

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
        addWelcomeMessage()
    }

    // ---------------- CHAT ----------------
    fun sendMessage(userMessage: String) {

        if (userMessage.isBlank()) return

        // ✅ USER MESSAGE (FIXED)
        _chatMessages.value += ChatMessage(
            content = userMessage,
            isUser = true
        )

        _chatLoading.value = true

        viewModelScope.launch {

            try {

                val model = generativeModel

                // ❗ SAFE NULL CHECK
                if (model == null) {
                    _chatMessages.value += ChatMessage(
                        content = "AI not initialized. Check API key.",
                        isUser = false
                    )
                    return@launch
                }

                val prompt = """
                    You are a farming assistant.
                    Answer in ${_selectedLanguage.value.displayName}.
                    
                    Question:
                    $userMessage
                """.trimIndent()

                val response = model.generateContent(
                    content { text(prompt) }
                )

                val aiText = response.text ?: "No response from AI."

                // ✅ AI MESSAGE (FIXED)
                _chatMessages.value += ChatMessage(
                    content = aiText,
                    isUser = false
                )

            } catch (e: Exception) {

                e.printStackTrace()

                val errorMsg = when (_selectedLanguage.value) {
                    AppLanguage.KANNADA -> "AI ದೋಷವಾಗಿದೆ"
                    AppLanguage.HINDI -> "AI त्रुटि"
                    AppLanguage.TELUGU -> "AI లోపం"
                    else -> "AI error occurred"
                }

                _chatMessages.value += ChatMessage(
                    content = errorMsg,
                    isUser = false
                )

            } finally {
                _chatLoading.value = false
            }
        }
    }

    // ---------------- SEARCH ----------------
    fun searchCrops(query: String) {
        _searchQuery.value = query

        _filteredCrops.value =
            if (query.isBlank()) {
                CropRepository.allCrops
            } else {
                CropRepository.allCrops.filter {
                    it.nameEn.contains(query, true) ||
                            it.nameKn.contains(query) ||
                            it.nameHi.contains(query) ||
                            it.nameTe.contains(query)
                }
            }
    }

    fun updateProfile(profile: FarmerProfile) {
        _farmerProfile.value = profile
    }
}