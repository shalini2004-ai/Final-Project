package com.raitha.bharosa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.raitha.bharosa.data.models.AppLanguage
import com.raitha.bharosa.ui.screens.*
import com.raitha.bharosa.ui.theme.FarmGreen
import com.raitha.bharosa.ui.theme.RaithaBharosaTheme
import com.raitha.bharosa.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RaithaBharosaTheme {
                RaithaBharosaApp()
            }
        }
    }
}

enum class Screen(
    val titleEn: String,
    val icon: ImageVector,
    val route: String
) {
    LOGIN("Login", Icons.Default.Home, "login"),
    HOME("Home", Icons.Default.Home, "home"),
    SOWING("Sowing", Icons.Default.Grass, "sowing"),
    MARKET("Market", Icons.Default.TrendingUp, "market"),
    CHAT("AI Expert", Icons.Default.SmartToy, "chat"),
    CROPS("Crops", Icons.Default.LocalFlorist, "crops")
}

@Composable
fun RaithaBharosaApp() {

    val viewModel: MainViewModel = viewModel()

    // SAFE STATE COLLECTION (prevents crash)
    val selectedLanguage by viewModel.selectedLanguage.collectAsState(initial = AppLanguage.ENGLISH)
    val sowingWindows by viewModel.sowingWindows.collectAsState(initial = emptyList())
    val marketRates by viewModel.marketRates.collectAsState(initial = emptyList())
    val weather by viewModel.weather.collectAsState(initial = null)
    val chatMessages by viewModel.chatMessages.collectAsState(initial = emptyList())
    val chatLoading by viewModel.chatLoading.collectAsState(initial = false)
    val filteredCrops by viewModel.filteredCrops.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState(initial = "")

    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

    val getNavLabel: (Screen) -> String = { screen ->
        when (screen) {
            Screen.LOGIN -> "Login"
            Screen.HOME -> "Home"
            Screen.SOWING -> "Sowing"
            Screen.MARKET -> "Market"
            Screen.CHAT -> "AI Expert"
            Screen.CROPS -> "Crops"
        }
    }

    Scaffold(
        bottomBar = {
            if (currentScreen != Screen.LOGIN) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {

                    // FIXED: Screen.entries instead of values()
                    Screen.entries
                        .filter { it != Screen.LOGIN }
                        .forEach { screen ->

                            NavigationBarItem(
                                selected = currentScreen == screen,
                                onClick = { currentScreen = screen },
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.titleEn,
                                        tint = if (currentScreen == screen) FarmGreen else Color.Gray
                                    )
                                },
                                label = {
                                    Text(
                                        text = getNavLabel(screen),
                                        fontSize = 10.sp,
                                        color = if (currentScreen == screen) FarmGreen else Color.Gray
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = FarmGreen.copy(alpha = 0.15f)
                                )
                            )
                        }
                }
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {

            when (currentScreen) {

                Screen.LOGIN -> {
                    LoginScreen(
                        onLoginSuccess = {
                            currentScreen = Screen.HOME
                        }
                    )
                }

                Screen.HOME -> {
                    HomeScreen(
                        weather = weather,
                        sowingWindows = sowingWindows,
                        marketRates = marketRates,
                        selectedLanguage = selectedLanguage,
                        onNavigateToSowing = { currentScreen = Screen.SOWING },
                        onNavigateToMarket = { currentScreen = Screen.MARKET },
                        onNavigateToChat = { currentScreen = Screen.CHAT },
                        onNavigateToCrops = { currentScreen = Screen.CROPS }
                    )
                }

                Screen.SOWING -> SowingScreen(
                    sowingWindows = sowingWindows,
                    selectedLanguage = selectedLanguage
                )

                Screen.MARKET -> MarketScreen(
                    marketRates = marketRates,
                    selectedLanguage = selectedLanguage
                )

                Screen.CHAT -> ChatScreen(
                    messages = chatMessages,
                    isLoading = chatLoading,
                    selectedLanguage = selectedLanguage,
                    onSendMessage = viewModel::sendMessage,
                    onLanguageChange = viewModel::setLanguage
                )

                Screen.CROPS -> CropsScreen(
                    crops = filteredCrops,
                    searchQuery = searchQuery,
                    onSearchChange = viewModel::searchCrops,
                    selectedLanguage = selectedLanguage
                )
            }
        }
    }
}
